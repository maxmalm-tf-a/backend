package com.example;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import net.sf.json.JSONObject;

/**
 * Root resource (exposed at "api" path)
 */
@Path("api")
public class MyResource extends Application {
    
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // Register my custom provider.
        classes.add(CORS.class);
        System.out.println("asdasd");
        return classes;
    }
    
    Database db = new Database ();
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBase() {
        System.out.println("123123");
        return Response.ok("hello").header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@Context UriInfo info) {
        String username = info.getQueryParameters().getFirst("username");
        String password = info.getQueryParameters().getFirst("password");
        
        String token;
        try {
            token = db.verifyCredentials(username, password);
        }
        catch(IllegalArgumentException e) {
            return Response.status(401).entity(JSONUtil.String2JSONStringError(e.getMessage())).header("Access-Control-Allow-Origin", "*").build();
        }
        return Response.ok(JSONUtil.String2JSONString(token)).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("verifytoken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyToken(@Context UriInfo info) {
        String token = info.getQueryParameters().getFirst("token");
        int userid;
        try {
            userid = db.checkToken(token);
            System.out.println(userid);
        }
        catch(IllegalArgumentException e) {
            return Response.status(401).entity(JSONUtil.String2JSONStringError(e.getMessage())).header("Access-Control-Allow-Origin", "*").build();
        }
        return Response.ok(JSONUtil.Int2JSONString(userid)).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @POST
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(@Context UriInfo info) {
        String username = info.getQueryParameters().getFirst("username");
        String password = info.getQueryParameters().getFirst("password");
        String token = null;
        
        if(db.checkUserFree(username)) {
            token = db.createUser(username, password);
        }
        else {
            return Response.status(401).entity("Username taken").header("Access-Control-Allow-Origin", "*").build();
        }
        return Response.ok(token).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @GET
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactions(@Context UriInfo info) {
        String token = info.getQueryParameters().getFirst("token");
        ResultSet rs = db.getTransactions(token);
        String result = JSONUtil.ResultSet2JSONString(rs);
        return Response.ok(result).header("Access-Control-Allow-Origin", "*").build();
    }
    
    @POST
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public String postTransactions(@Context UriInfo info) {
        
        String token = info.getQueryParameters().getFirst("token");
        int userId;
        try {
            userId = db.checkToken(token);
        }
        catch(IllegalArgumentException e) {
            System.out.print(e.getMessage());
            return "error";
        }
        return "New transaction for" + token + userId;
    }
    
    @GET
    @Path("populate") // could not use "seed" here for some reason
    @Produces(MediaType.APPLICATION_JSON)
    public Response postSeedTransactions(@Context UriInfo info) {
        
        System.out.println("hi!");
        
        String token = info.getQueryParameters().getFirst("token");
        int userId;
        try {
            userId = db.checkToken(token);
        }
        catch(IllegalArgumentException e) {
            System.out.print(e.getMessage());
            return Response.status(401).entity("Wrong token").header("Access-Control-Allow-Origin", "*").build();
        }
        db.clearTransactions(token);
        db.insertTransactions(token, "Lön", 40000, "Inkomst", 30);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 29);
        db.insertTransactions(token, "Hyra", -5000, "Bostad", 28);
        db.insertTransactions(token, "Avanza", -2000, "Sparande", 27);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 26);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 25);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 24);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 23);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 22);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 21);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 20);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 19);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 18);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 17);
        db.insertTransactions(token, "Systembolaget", -493, "Nöje", 16);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 15);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 14);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 13);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 12);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 11);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 9);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 8);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 7);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 6);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 5);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 4);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 3);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 2);
        db.insertTransactions(token, "Kaffe", -40, "Nöje", 1);
        
        return Response.ok(JSONUtil.String2JSONString("nice")).header("Access-Control-Allow-Origin", "*").build();
    }
}
