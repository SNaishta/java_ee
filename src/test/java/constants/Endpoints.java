package constants;

public interface Endpoints {
    String address = "localhost";
    String port = "3000";

    String baseUrl = String.format("http://%s:%s", address, port);

    String login = "/rest/user/login";

    String searchAllProducts = "/rest/products/search";

    String reviewEndpoint = "/rest/products/{product-id}/reviews";




}
