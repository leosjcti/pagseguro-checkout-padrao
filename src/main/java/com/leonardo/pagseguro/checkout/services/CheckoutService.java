package com.leonardo.pagseguro.checkout.services;

import com.leonardo.pagseguro.checkout.vo.CheckoutVO;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CheckoutService {

    @Value("${checkout.url}")
    private String URL;

    @Value("${checkout.url.session}")
    private String URL_SESSION;

    @Value("${checkout.url.processando}")
    private String URL_PROCESSANDO;

    @Value("${checkout.url.alterar.status}")
    private String URL_ALTERAR_STATUS;

    @Value("${checkout.url.extorno}")
    private String URL_EXTORNO;

    @Value("${email.sendbox}")
    private String EMAIL_SENDBOX;

    @Value("${token.sendbox}")
    private String TOKEN_SENDBOX;


    /**
     * getSessao
     * @return
     */
    public ResponseEntity getSessao() {
        String result = "";
        String json = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_SESSION);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);

            HttpPost postRequest = new HttpPost(builder.build());
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            JSONObject xmlJSONObj = XML.toJSONObject(result);
            json = xmlJSONObj.toString(4);
            System.out.println(json);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }


    /**
     *
     * @param checkoutVO
     * @return
     */
    public String processar(CheckoutVO checkoutVO) {
        String result = "";
        System.out.println(checkoutVO);
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_PROCESSANDO);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);

            HttpPost postRequest = new HttpPost(builder.build());
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("paymentMode", "default"));
            formParams.add(new BasicNameValuePair("paymentMethod", "creditCard"));
            formParams.add(new BasicNameValuePair("currency", "BRL"));
            formParams.add(new BasicNameValuePair("extraAmount", "0.00"));
            formParams.add(new BasicNameValuePair("itemId1", "0001"));
            formParams.add(new BasicNameValuePair("itemDescription1", "Notebook"));
            formParams.add(new BasicNameValuePair("itemAmount1", "300.00"));
            formParams.add(new BasicNameValuePair("itemQuantity1", "1"));
            formParams.add(new BasicNameValuePair("notificationURL", "https://www.meusite.com.br/notificacao.php"));
            formParams.add(new BasicNameValuePair("reference", checkoutVO.getRef()));
            formParams.add(new BasicNameValuePair("senderName", checkoutVO.getNomeComprador()));
            formParams.add(new BasicNameValuePair("senderCPF", checkoutVO.getCpfComprador()));
            formParams.add(new BasicNameValuePair("senderAreaCode", checkoutVO.getDdd()));
            formParams.add(new BasicNameValuePair("senderPhone", checkoutVO.getTelefone()));
            formParams.add(new BasicNameValuePair("senderEmail", "c04994478079526890327@sandbox.pagseguro.com.br"));
            formParams.add(new BasicNameValuePair("senderHash", checkoutVO.getHashCard())); //Hash do Comprador
            formParams.add(new BasicNameValuePair("shippingAddressStreet", checkoutVO.getEndereco()));
            formParams.add(new BasicNameValuePair("shippingAddressNumber", checkoutVO.getNumero()));
            formParams.add(new BasicNameValuePair("shippingAddressComplement", checkoutVO.getComplemento()));
            formParams.add(new BasicNameValuePair("shippingAddressDistrict", checkoutVO.getBairro()));
            formParams.add(new BasicNameValuePair("shippingAddressPostalCode", checkoutVO.getCep()));
            formParams.add(new BasicNameValuePair("shippingAddressCity", checkoutVO.getCidade()));
            formParams.add(new BasicNameValuePair("shippingAddressState", checkoutVO.getUf()));
            formParams.add(new BasicNameValuePair("shippingAddressCountry", "BRA"));
            formParams.add(new BasicNameValuePair("shippingType", "1"));
            formParams.add(new BasicNameValuePair("shippingCost", "0.00"));
            formParams.add(new BasicNameValuePair("creditCardToken", checkoutVO.getTokenCard())); //Token do Cartão
            formParams.add(new BasicNameValuePair("installmentQuantity", "1"));
            formParams.add(new BasicNameValuePair("installmentValue", "300.00"));
            formParams.add(new BasicNameValuePair("noInterestInstallmentQuantity", "2"));
            formParams.add(new BasicNameValuePair("creditCardHolderName", checkoutVO.getNomeComprador()));
            formParams.add(new BasicNameValuePair("creditCardHolderCPF", checkoutVO.getCpfComprador()));
            formParams.add(new BasicNameValuePair("creditCardHolderBirthDate", "27/10/1987"));
            formParams.add(new BasicNameValuePair("creditCardHolderAreaCode", checkoutVO.getDdd()));
            formParams.add(new BasicNameValuePair("creditCardHolderPhone", checkoutVO.getTelefone()));
            formParams.add(new BasicNameValuePair("billingAddressStreet", checkoutVO.getEndereco()));
            formParams.add(new BasicNameValuePair("billingAddressNumber", checkoutVO.getNumero()));
            formParams.add(new BasicNameValuePair("billingAddressComplement", checkoutVO.getComplemento()));
            formParams.add(new BasicNameValuePair("billingAddressDistrict", checkoutVO.getBairro()));
            formParams.add(new BasicNameValuePair("billingAddressPostalCode", checkoutVO.getCep()));
            formParams.add(new BasicNameValuePair("billingAddressCity", checkoutVO.getCidade()));
            formParams.add(new BasicNameValuePair("billingAddressState", checkoutVO.getUf()));
            formParams.add(new BasicNameValuePair("billingAddressCountry", "BRA"));

            postRequest.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
            CloseableHttpResponse response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            System.out.println(result);
            client.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param ref
     * @return
     */
    public ResponseEntity consultarTransacao(String ref) {
        String result = "";
        String json = null;
        CloseableHttpResponse response = null;
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_PROCESSANDO);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);
            builder.setParameter("reference", ref);

            HttpGet postRequest = new HttpGet(builder.build());

            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

            response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            JSONObject xmlJSONObj = XML.toJSONObject(result);
            json = xmlJSONObj.toString(4);
            System.out.println(json);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }

    /**
     * getTransacao
     * @return
     */
    public StatusLine confirmarPagamento(String cod) {
        String result = "";
        String json = null;
        cod = cod.replace("cod=", "");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_ALTERAR_STATUS+cod.replace("-", "")+"/status");

            HttpPatch request = new HttpPatch(builder.build());
            String param = "{\"status_to\": \"3\"}";

            StringEntity entity = new StringEntity(param);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

            request.setEntity(entity);
            request.setHeader("Authorization", TOKEN_SENDBOX);
            request.setHeader("Content-type", "application/json");

            HttpResponse response = httpClient.execute(request);

            result = EntityUtils.toString(response.getEntity());
            JSONObject xmlJSONObj = XML.toJSONObject(result);
            json = xmlJSONObj.toString(4);
            System.out.println(json);
            httpClient.close();
            return response.getStatusLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param cod
     * @return
     */
    public Object extornarPagamento(String cod) {
        String result = "";
        String json = null;
        String cod2 = cod.replace("cod=", "");
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_EXTORNO);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);
            builder.setParameter("transactionCode", cod2.replace("-", ""));

            HttpPost postRequest = new HttpPost(builder.build());
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            //List<NameValuePair> formParams = new ArrayList<>();
            //formParams.add(new BasicNameValuePair("transactionCode", cod2.replace("-", "")));
            //postRequest.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));

            CloseableHttpResponse response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            System.out.println(result);
            JSONObject xmlJSONObj = XML.toJSONObject(result);
            json = xmlJSONObj.toString(4);
            System.out.println(json);
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(json, HttpStatus.OK);
    }


    /**
     *
     * @param checkoutVO
     * @return
     */
    public Object obterAutorizacao(CheckoutVO checkoutVO) {
        String result = "";
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);

            HttpPost postRequest = new HttpPost(builder.build());
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("email", checkoutVO.getEmail()));
            formParams.add(new BasicNameValuePair("token", checkoutVO.getToken()));
            formParams.add(new BasicNameValuePair("currency", "BRL"));
            formParams.add(new BasicNameValuePair("itemId1", "0001"));
            formParams.add(new BasicNameValuePair("itemDescription1", "Notebook Prata"));
            formParams.add(new BasicNameValuePair("itemAmount1", "100.00"));
            formParams.add(new BasicNameValuePair("itemQuantity1", "1"));
            formParams.add(new BasicNameValuePair("itemWeight1", "1000"));
            formParams.add(new BasicNameValuePair("reference", "REF1234"));
            formParams.add(new BasicNameValuePair("senderName", "Jose Comprador"));
            formParams.add(new BasicNameValuePair("senderAreaCode", "11"));
            formParams.add(new BasicNameValuePair("senderPhone", "56713293"));
            formParams.add(new BasicNameValuePair("senderCPF", "38440987803"));
            formParams.add(new BasicNameValuePair("senderBornDate", "12/03/1990"));
            formParams.add(new BasicNameValuePair("senderEmail",
                    "email@sandbox.pagseguro.com.br"));
            formParams.add(new BasicNameValuePair("shippingType", "1"));
            formParams.add(new BasicNameValuePair("shippingAddressStreet", "Av.Brig.Faria Lima"));
            formParams.add(new BasicNameValuePair("shippingAddressNumber", "1384"));
            formParams.add(new BasicNameValuePair("shippingAddressComplement", "2o andar"));
            formParams.add(new BasicNameValuePair("shippingAddressDistrict", "Jardim Paulistano"));
            formParams.add(new BasicNameValuePair("shippingAddressPostalCode", "01452002"));
            formParams.add(new BasicNameValuePair("shippingAddressCity", "Sao Paulo"));
            formParams.add(new BasicNameValuePair("shippingAddressState", "SP"));
            formParams.add(new BasicNameValuePair("shippingAddressCountry", "BRA"));
            formParams.add(new BasicNameValuePair("extraAmount", "-0.01"));
            formParams.add(new BasicNameValuePair("redirectURL", "http://www.seusite.com.br"));
            formParams.add(new BasicNameValuePair("notificationURL",
                    "https://yourserver.com/nas_ecommerce/277be731-3b7c-4dac-8c4e-4c3f4a1fdc46/"));
            formParams.add(new BasicNameValuePair("maxUses", "1"));
            formParams.add(new BasicNameValuePair("maxAge", "3000"));
            formParams.add(new BasicNameValuePair("shippingCost", "1.00"));

            postRequest.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
            CloseableHttpResponse response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            System.out.println(result);
            client.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     *
     * @param checkoutVO
     * @return
     */
    public Object processarSplit(@RequestBody CheckoutVO checkoutVO) {
        String result = "";
        try {
            CloseableHttpClient client = HttpClients.createDefault();

            String template = URL_PROCESSANDO+"/?appId=f03a62de-513b-45d3-85fd-24ce2181496fb21b6277450297ffb76a6acacd644544ef71-8a4d-49e4-9f81-aa94604b17e6&appKey=f03a62de-513b-45d3-85fd-24ce2181496fb21b6277450297ffb76a6acacd644544ef71-8a4d-49e4-9f81-aa94604b17e6";
            System.out.println(template);

            URIBuilder builder = new URIBuilder(template);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);

            HttpPost postRequest = new HttpPost(builder.build());
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("email", "leo.sjc.ti@gmail.com"));
            formParams.add(new BasicNameValuePair("token", "f03a62de-513b-45d3-85fd-24ce2181496fb21b6277450297ffb76a6acacd644544ef71-8a4d-49e4-9f81-aa94604b17e6"));
            formParams.add(new BasicNameValuePair("currency", "BRL"));
            formParams.add(new BasicNameValuePair("itemId1", "0001"));
            formParams.add(new BasicNameValuePair("itemDescription1", "Notebook Prata"));
            formParams.add(new BasicNameValuePair("itemAmount1", "300.00"));
            formParams.add(new BasicNameValuePair("itemQuantity1", "1"));
            formParams.add(new BasicNameValuePair("itemWeight1", "1000"));
            formParams.add(new BasicNameValuePair("reference", "REF1234"));
            formParams.add(new BasicNameValuePair("senderName", checkoutVO.getNomeComprador()));
            formParams.add(new BasicNameValuePair("senderAreaCode", "11"));
            formParams.add(new BasicNameValuePair("senderPhone", "56713293"));
            formParams.add(new BasicNameValuePair("senderCPF", "38440987803"));
            formParams.add(new BasicNameValuePair("senderBornDate", "12/03/1990"));
            formParams.add(new BasicNameValuePair("senderEmail","email@sandbox.pagseguro.com.br"));
            //formParams.add(new BasicNameValuePair("shippingType", "1"));
            formParams.add(new BasicNameValuePair("shippingAddressRequired", "false"));
            //formParams.add(new BasicNameValuePair("shippingAddressDistrict", "Jardim Paulistano"));
            //formParams.add(new BasicNameValuePair("shippingAddressState", "SP"));
            //formParams.add(new BasicNameValuePair("shippingAddressCountry", "BRA"));
            formParams.add(new BasicNameValuePair("extraAmount", "-0.01"));
            formParams.add(new BasicNameValuePair("redirectURL", "http://www.seusite.com.br"));
            formParams.add(new BasicNameValuePair("notificationURL",
                    "https://yourserver.com/nas_ecommerce/277be731-3b7c-4dac-8c4e-4c3f4a1fdc46/"));
            formParams.add(new BasicNameValuePair("maxUses", "1"));
            formParams.add(new BasicNameValuePair("maxAge", "3000"));
            formParams.add(new BasicNameValuePair("shippingCost", "0.00"));

            //Obrigatorios vindo do Front
            formParams.add(new BasicNameValuePair("creditCardToken", checkoutVO.getTokenCard()));
            formParams.add(new BasicNameValuePair("billingAddressPostalCode", "01452002"));
            formParams.add(new BasicNameValuePair("billingAddressStreet", "Av. Brig. Faria Lima"));
            formParams.add(new BasicNameValuePair("billingAddressNumber", "1384"));
            formParams.add(new BasicNameValuePair("billingAddressCity", "Sao Paulo"));
            formParams.add(new BasicNameValuePair("billingAddressDistrict", "Jardim Paulistano"));
            formParams.add(new BasicNameValuePair("billingAddressState", "SP"));
            formParams.add(new BasicNameValuePair("billingAddressCountry", "BRA"));
            formParams.add(new BasicNameValuePair("installmentQuantity", checkoutVO.getInstallmentQuantity()));
            formParams.add(new BasicNameValuePair("installmentValue", "300.00"));
            //formParams.add(new BasicNameValuePair("installmentValue", checkoutVO.getInstallmentValue()));
            formParams.add(new BasicNameValuePair("creditCardHolderName", "Leonardo Melo de Lima"));
            formParams.add(new BasicNameValuePair("creditCardHolderCPF", "34096016861"));
            formParams.add(new BasicNameValuePair("creditCardHolderBirthDate", "27/10/1987"));
            formParams.add(new BasicNameValuePair("creditCardHolderAreaCode", "12"));
            formParams.add(new BasicNameValuePair("creditCardHolderPhone", "991829674"));


            postRequest.setEntity(new UrlEncodedFormEntity(formParams, Consts.UTF_8));
            CloseableHttpResponse response = client.execute(postRequest);
            result = EntityUtils.toString(response.getEntity());

            System.out.println(result);
            client.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
