package com.example;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

// http://www.mkyong.com/webservices/jax-rs/jax-rs-queryparam-example/

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("api")
public class MyResource {
    
    
    Database db = new Database ();
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getBase() {
        return "Api base";
    }
    
    @GET
    @Path("account")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAccount(@Context UriInfo info) {
        String username = info.getQueryParameters().getFirst("username");
        String password = info.getQueryParameters().getFirst("password");
        
        String token = "";
        try {
            token = db.verifyCredentials(username, password);
        }
        catch(IllegalArgumentException e) {
            System.out.print(e.getMessage());
        }
        return token;
    }
    
    @GET
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTransactions() {
        return "My transactions";
    }
    
    @POST
    @Path("transactions")
    @Produces(MediaType.APPLICATION_JSON)
    public String postTransactions(@Context UriInfo info) {
        String token = info.getQueryParameters().getFirst("token");
        String userId = "error";
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
