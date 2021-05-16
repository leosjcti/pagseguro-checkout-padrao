package com.leonardo.pagseguro.checkout.controllers;

import com.leonardo.pagseguro.checkout.services.CheckoutService;
import com.leonardo.pagseguro.checkout.vo.CheckoutVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout/")
@CrossOrigin(origins = "*")
public class CheckoutController {

    @Autowired
    CheckoutService service;

    /**
     * obterAutorizacao
     * @param checkoutVO
     * @return
     */
    @GetMapping(produces = { "application/json", "application/xml"})
    public Object obterAutorizacao(@RequestBody CheckoutVO checkoutVO) {
        return service.obterAutorizacao(checkoutVO);
    }

    /**
     * getSessao
     * @return
     */
    @GetMapping(value = "/getSessao")
    public ResponseEntity getSessao() {
       return service.getSessao();
    }


    /**
     * processar
     * @param checkoutVO
     * @return
     */
    //Checkout Transparente com Cartão de Crédito
    @PostMapping(value = "/processar")
    public String processar(@RequestBody CheckoutVO checkoutVO) {
       return service.processar(checkoutVO);
    }


    /**
     * @param checkoutVO
     * @return
     */
    //Checkout Padrão
    @PostMapping(value = "/processarSplit")
    public Object processarSplit(@RequestBody CheckoutVO checkoutVO) {
        return service.processarSplit(checkoutVO);
    }
}
