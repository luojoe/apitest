package com.zhaow.restful.common;

import com.zhaow.utils.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class RequestTestHelper {
    private static final int defaultStatus = 404;

    public static RespondTestEntity request(String url, String method) {
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setUrl(url);
        respondTestEntity.setStatus(defaultStatus);
        if (method == null) {
            return respondTestEntity;
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        switch (method.toUpperCase()) {
            case "GET": return get(url);
            case "POST": return post(url);
            case "PUT":  return put(url);
            case "DELETE": return delete(url);
            default:return respondTestEntity;
        }

    }

    public static RespondTestEntity get(String url) {
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setStatus(defaultStatus);
        respondTestEntity.setUrl(url);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpMethod = new HttpGet(completed(url));
        try {
            response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();
            String back = toString(entity);
            respondTestEntity.setStatus(response.getStatusLine().getStatusCode());
            respondTestEntity.setRespond(back);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, httpClient);
        }

        return respondTestEntity;
    }

    public static RespondTestEntity post(String url) {
        List<BasicNameValuePair> params = new ArrayList<>();
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setStatus(defaultStatus);
        respondTestEntity.setUrl(url);

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpEntity httpEntity;
            httpEntity = new UrlEncodedFormEntity(params);
            HttpPost httpMethod = new HttpPost(completed(url));
            httpMethod.setEntity(httpEntity);
            response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();
            String back = toString(entity);
            respondTestEntity.setStatus(response.getStatusLine().getStatusCode());
            respondTestEntity.setRespond(back);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, httpClient);
        }

        return respondTestEntity;
    }


    public static RespondTestEntity put(String url) {
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setStatus(defaultStatus);
        respondTestEntity.setUrl(url);

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpPut httpMethod = new HttpPut(completed(url));
            response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();
            String back = toString(entity);
            respondTestEntity.setStatus(response.getStatusLine().getStatusCode());
            respondTestEntity.setRespond(back);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, httpClient);
        }

        return respondTestEntity;
    }


    public static RespondTestEntity delete(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setStatus(defaultStatus);
        respondTestEntity.setUrl(url);

        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpDelete httpMethod = new HttpDelete(url);
            response = httpClient.execute(httpMethod);
            HttpEntity entity = response.getEntity();
            String back = toString(entity);
            respondTestEntity.setStatus(response.getStatusLine().getStatusCode());
            respondTestEntity.setRespond(back);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, httpClient);
        }

        return respondTestEntity;
    }


    public static RespondTestEntity postRequestBodyWithJson(String url, String json) {
        RespondTestEntity respondTestEntity = new RespondTestEntity();
        respondTestEntity.setStatus(defaultStatus);
        respondTestEntity.setUrl(url);
        respondTestEntity.setParam(json);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost postMethod = new HttpPost(completed(url));
        try {
            StringEntity httpEntity = new StringEntity(json);

            httpEntity.setContentType("application/json");
            httpEntity.setContentEncoding("UTF-8");
            postMethod.setHeader("Accept", "application/json");
            postMethod.addHeader("Content-type","application/json; charset=utf-8");
//            postMethod.setEntity(new StringEntity(parameters, Charset.forName("UTF-8")));
            postMethod.setEntity(httpEntity);                                          //设置post请求实体

            response = httpClient.execute(postMethod);
            HttpEntity entity = response.getEntity();
            String back = toString(entity);
            respondTestEntity.setStatus(response.getStatusLine().getStatusCode());
            respondTestEntity.setRespond(back);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            release(response, httpClient);
        }
        return respondTestEntity;
    }

    private static void release(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        if (response != null) {
            try {
                response.close();
            } catch (IOException e) { }
        }
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) { }
        }
    }

    private static String completed(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }
    @NotNull
    private static String toString(HttpEntity entity) {
        String result = null;
        try {
            result = EntityUtils.toString(entity, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(result != null && JsonUtils.isValidJson(result))
            return JsonUtils.format(result);

        return result;
    }

}
