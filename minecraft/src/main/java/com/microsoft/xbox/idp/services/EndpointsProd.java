package com.microsoft.xbox.idp.services;

class EndpointsProd implements Endpoints {
    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String accounts() {
        return "https://accounts.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String privacy() {
        return "https://privacy.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String profile() {
        return "https://profile.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String userAccount() {
        return "https://accountstroubleshooter.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String userManagement() {
        return "https://user.mgt.xboxlive.com";
    }

    EndpointsProd() {
    }
}
