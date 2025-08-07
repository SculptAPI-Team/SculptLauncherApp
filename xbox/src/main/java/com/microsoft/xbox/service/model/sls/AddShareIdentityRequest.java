package com.microsoft.xbox.service.model.sls;

import com.microsoft.xbox.toolkit.GsonUtil;

import java.util.ArrayList;

/**
 * 07.01.2021
 *
 * @author Тимашков Иван
 * @author https://github.com/TimScriptov
 */

public class AddShareIdentityRequest {
    public ArrayList<String> xuids;

    public AddShareIdentityRequest(ArrayList<String> arrayList) {
        this.xuids = arrayList;
    }

    public static String getAddShareIdentityRequestBody(AddShareIdentityRequest addShareIdentityRequest) {
        return GsonUtil.toJsonString(addShareIdentityRequest);
    }
}
