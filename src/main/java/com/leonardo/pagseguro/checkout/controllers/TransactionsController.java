package com.leonardo.pagseguro.checkout.controllers;

import com.leonardo.pagseguro.checkout.services.CheckoutService;
import com.leonardo.pagseguro.checkout.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions/")
@CrossOrigin(origins = "*")
public class TransactionsController {

    @Autowired
    TransactionsService service;

    @GetMapping(value = "/getSessao")
    public ResponseEntity getSessao() {
        return service.getSessao();
    }

    /**
     * consultarTransacao
     * @return
     */
    @GetMapping(value = "/consultarTransacao/{ref}")
    public ResponseEntity consultarTransacao(@PathVariable("ref") String ref) {
        return service.consultarTransacao(ref);
    }


    /**
     * confirmarPagamento
     * @return
     */
    @RequestMapping(value="/confirmarPagamento",method = RequestMethod.POST)
    public Object confirmarPagamento(@RequestBody String cod) {
        return service.confirmarPagamento(cod);
    }


    /**
     * cancelarPagamento
     * @return
     */
    @RequestMapping(value="/cancelarPagamento",method = RequestMethod.POST)
    public Object cancelarPagamento(@RequestBody String cod) {
        return service.cancelarPagamento(cod);
    }


    /**
     * extornarPagamento
     * @param cod
     * @return
     */
    @RequestMapping(value="/extornarPagamento",method = RequestMethod.POST)
    public Object extornarPagamento(@RequestBody String cod) {
        return service.extornarPagamento(cod);
    }
}
