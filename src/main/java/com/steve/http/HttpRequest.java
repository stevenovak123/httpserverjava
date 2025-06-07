package com.steve.http;

public class HttpRequest extends HttpMessage {
    private HttpMethod method;
    private String requestTarget;
    private String httpVersion; // from the request
    private HttpVersions compatibleVersion;

    // Package level to ensure any file in this package can instantiate an object of this
    HttpRequest() {
    }

    public HttpVersions getCompatibleVersion() {
        return compatibleVersion;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String methodName) throws HttpParsingException {
        for (HttpMethod method : HttpMethod.values()) {
            if (methodName.equals(method.name())) {
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isEmpty()) {
            throw new HttpParsingException(HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
        this.requestTarget = requestTarget;
    }

    void setHttpVersion(String httpVersion) throws BadHttpVersionException, HttpParsingException {
        this.httpVersion = httpVersion;
        this.compatibleVersion = HttpVersions.getCompatibleVersion(httpVersion);
    if(this.compatibleVersion ==null){
        throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
    }
    }
}
