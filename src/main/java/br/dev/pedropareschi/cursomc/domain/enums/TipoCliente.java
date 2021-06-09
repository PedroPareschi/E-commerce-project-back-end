package br.dev.pedropareschi.cursomc.domain.enums;

public enum TipoCliente {
    PESSOA_FISICA(1, "Pessoa Física"),
    PESSOA_JURIDICA(2, "Pessoa Jurídica");

    private final Integer cod;
    private final String descricao;

    TipoCliente(Integer cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Integer getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCliente toEnum(Integer cod){
        if(cod == null){
            return null;
        }
        for(TipoCliente x: TipoCliente.values()){
            if(cod.equals(x.getCod())){
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}
