package com.leonardo.pagseguro.checkout.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CheckoutVO {
    private String email;
    private String token;

    private String nomeComprador;
    private String cpfComprador;
    private String ddd;
    private String telefone;
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String tokenCard;
    private String installmentQuantity;
    private String installmentValue;
    private String hashCard;
    private String ref;
}
