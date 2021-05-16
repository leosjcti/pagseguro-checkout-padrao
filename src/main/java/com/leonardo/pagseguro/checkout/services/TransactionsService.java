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
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionsService {

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

    @Value("${checkout.url.cancelamento}")
    private String URL_CANCELAMENTO;

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
     *
     * @param cod
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
        cod = cod.replace("cod=", "");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_EXTORNO);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);
            builder.setParameter("transactionCode", cod.replace("-", ""));

            HttpPost request = new HttpPost(builder.build());
            String param = "{\"transactionCode\": \""+cod.replace("-", "")+"\"}";

            StringEntity entity = new StringEntity(param);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

            request.setEntity(entity);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

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
    public Object cancelarPagamento(String cod) {
        String result = "";
        String json = null;
        cod = cod.replace("cod=", "");

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            URIBuilder builder = new URIBuilder(URL_CANCELAMENTO);
            builder.setParameter("email", EMAIL_SENDBOX);
            builder.setParameter("token", TOKEN_SENDBOX);
            builder.setParameter("transactionCode", cod.replace("-", ""));

            HttpPost request = new HttpPost(builder.build());
            String param = "{\"transactionCode\": \""+cod.replace("-", "")+"\"}";

            StringEntity entity = new StringEntity(param);
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());

            request.setEntity(entity);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");

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

}
