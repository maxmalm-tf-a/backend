package com.example;

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
    public Response getTransactions() {
        return Response.ok(JSONUtil.String2JSONString("My transactions")).header("Access-Control-Allow-Origin", "*").build();
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
}
