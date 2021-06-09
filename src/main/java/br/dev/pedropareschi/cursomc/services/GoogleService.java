package br.dev.pedropareschi.cursomc.services;

import br.dev.pedropareschi.cursomc.services.exceptions.FileException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class GoogleService {

    private Logger LOG = LoggerFactory.getLogger(GoogleService.class);
    @Autowired
    private AmazonS3 googleClient;

    @Value("${gcp.bucket}")
    private String bucketName;

    public URI uploadFile(MultipartFile multipartFile){
        try {
            String fileName = multipartFile.getOriginalFilename();
            InputStream is = null;
            is = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            return uploadFile(is, fileName, contentType);
        } catch (IOException e) {
            throw new FileException("Erro de IO: " + e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String fileName, String contentType){
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType(contentType);
            LOG.info("iniciando upload");
            meta.setCacheControl("Cache-Control:private, max-age=0, no-transform");
            googleClient.putObject(bucketName, fileName, is, meta);
            LOG.info("upload concluido");
            return googleClient.getUrl(bucketName, fileName).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Erro ao converter URL para URI");
        }
    }
}
