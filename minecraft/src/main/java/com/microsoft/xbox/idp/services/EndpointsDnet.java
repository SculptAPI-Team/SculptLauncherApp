package com.microsoft.xbox.idp.services;

class EndpointsDnet implements Endpoints {
    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String accounts() {
        return "https://accounts.dnet.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String privacy() {
        return "https://privacy.dnet.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String profile() {
        return "https://profile.dnet.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String userAccount() {
        return "https://accountstroubleshooter.dnet.xboxlive.com";
    }

    @Override // com.microsoft.xbox.idp.services.Endpoints
    public String userManagement() {
        return "https://user.mgt.dnet.xboxlive.com";
    }

    EndpointsDnet() {
    }
}
