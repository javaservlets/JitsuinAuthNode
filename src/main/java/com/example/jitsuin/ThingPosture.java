package com.example.jitsuin;

import com.sun.identity.shared.debug.Debug;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThingPosture {
    private final static String DEBUG_FILE = "Jitsuin";
    protected Debug debug = Debug.getInstance(DEBUG_FILE);
    public String bearer_token;

    public ThingPosture() { // only setting vals n via the constructor to make scratch testing as 'real' as possible
        log(" constructor: generating token ");
    }

    public void generateToken(String token_url, String tenant, String client_id, String client_secret, String client_resource) { //
        HttpPost http_post = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            http_post = new HttpPost(token_url + tenant + "/oauth2/v2.0/token");

            http_post.setHeader("Accept", "application/json");
            http_post.setHeader("Accept", "*/*");
            http_post.setHeader("Cache-Control", "no-cache");
            http_post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            http_post.setHeader("Connection", "keep-alive");

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("client_id", client_id));
            params.add(new BasicNameValuePair("client_secret", client_secret));
            params.add(new BasicNameValuePair("scope", client_resource));

            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            //params.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
            http_post.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpclient.execute(http_post);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                this.bearer_token = getBearer(EntityUtils.toString(responseEntity), "access_token");
            }
        } catch (Exception e) {
            log(" getToken.error: " + e.toString());
        }
    }

    private static String getBearer(String parent, String child) {
        String noise = "";
        try {
            JSONObject jobj = new JSONObject(parent);
            Object idtkn = jobj.getString(child);
            noise = idtkn.toString();
            if (noise.startsWith("[")) { // get only 'value' from "["value"]"
                noise = noise.substring(1, noise.length() - 1);
            }
            if (noise.startsWith("\"")) {
                noise = noise.substring(1, noise.length() - 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return noise;
        }
    }

    public String getStatus(String query_url, String device_id, String compliance_string) { //
        String compliance_status = "";
        HttpGet http_get = null;

        try {
            HttpClient httpclient = HttpClients.createDefault();
            http_get = new HttpGet(query_url + device_id);
            http_get.setHeader("Authorization", "Bearer " + this.bearer_token); // latter was retrieved on class instantiation
            http_get.setHeader("Content-Type", "application/json");

            HttpResponse response = httpclient.execute(http_get);
            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String entity_str = EntityUtils.toString(responseEntity);
                log("  get status: " + entity_str);

                if (entity_str.toLowerCase().contains(compliance_string)) { //graph api returns complianceState: compliant
                    compliance_status = "noncompliant";
                }
            } else {
                compliance_status = "connection error";
            }
            log("compliance state? " + compliance_status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compliance_status;
    }


    public void log(String str) {
        debug.error("+++  thingPosture:    " + str);
    }
}
