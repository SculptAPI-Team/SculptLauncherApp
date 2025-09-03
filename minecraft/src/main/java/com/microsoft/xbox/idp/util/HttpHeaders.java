package com.microsoft.xbox.idp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HttpHeaders {
    private final List<Header> headers = new ArrayList();

    public void add(String str, String str2) {
        this.headers.add(new Header(str, str2));
    }

    public Collection<Header> getAllHeaders() {
        return this.headers;
    }

    public Header getFirstHeader(String str) {
        if (str == null) {
            return null;
        }
        for (Header header : this.headers) {
            if (str.equals(header.key)) {
                return header;
            }
        }
        return null;
    }

    public Header getLastHeader(String str) {
        if (str == null) {
            return null;
        }
        for (int size = this.headers.size() - 1; size >= 0; size--) {
            Header header = this.headers.get(size);
            if (str.equals(header.key)) {
                return header;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[ ");
        Iterator<Header> it = this.headers.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        sb.append(" ]");
        return sb.toString();
    }

    public static class Header {
        private final String key;
        private final String value;

        public Header(String str, String str2) {
            this.key = str;
            this.value = str2;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("{ \"");
            sb.append(this.key).append("\": \"").append(this.value).append("\" }");
            return sb.toString();
        }
    }
}
