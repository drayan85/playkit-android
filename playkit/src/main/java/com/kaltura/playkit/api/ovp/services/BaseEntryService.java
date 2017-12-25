/*
 * ============================================================================
 * Copyright (C) 2017 Kaltura Inc.
 * 
 * Licensed under the AGPLv3 license, unless a different license for a
 * particular library is specified in the applicable library path.
 * 
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/agpl-3.0.html
 * ============================================================================
 */

package com.kaltura.playkit.api.ovp.services;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaltura.playkit.api.ovp.APIDefines;
import com.kaltura.playkit.api.ovp.OvpRequestBuilder;


/**
 * @hide
 */

public class BaseEntryService extends OvpService {

    /*public static RequestBuilder entryInfo(String baseUrl, String ks, int partnerId, String entryId) {

        MultiRequestBuilder multiRequestBuilder = (MultiRequestBuilder) OvpService.getMultirequest(baseUrl, ks, partnerId)
                .tag("mediaAsset-multi-get");

        if(TextUtils.isEmpty(ks)){
            multiRequestBuilder.add(OvpSessionService.anonymousSession(baseUrl, partnerId));

            ks = "{1:result:ks}";
        }

        return multiRequestBuilder.add(list(baseUrl, ks, entryId),
                getPlaybackContext(baseUrl, ks, entryId),
                MetaDataService.list(baseUrl,ks,entryId));
    }
*/
    public static OvpRequestBuilder list(String baseUrl, String ks, String entryId) {
        return new OvpRequestBuilder()
                .service("baseEntry")
                .action("list")
                .method("POST")
                .url(baseUrl)
                .tag("baseEntry-list")
                .params(getEntryListReqParams(ks, entryId));
    }

    private static JsonObject getEntryListReqParams(String ks, String entryId) {

        BaseEntryListParams baseEntryListParams = new BaseEntryListParams(ks);
        baseEntryListParams.filter.redirectFromEntryId = entryId;
        baseEntryListParams.responseProfile.fields = "id,name,dataUrl,duration,msDuration,flavorParamsIds,mediaType,type,tags";
        baseEntryListParams.responseProfile.type = APIDefines.ResponseProfileType.IncludeFields;

        return new Gson().toJsonTree(baseEntryListParams).getAsJsonObject();
    }

    public static OvpRequestBuilder getContextData(String baseUrl, String ks, String entryId) {
        JsonObject params = new JsonObject();
        params.addProperty("entryId", entryId);
        params.addProperty("ks", ks);

        JsonObject contextDataParams = new JsonObject();
        contextDataParams.addProperty("objectType","KalturaContextDataParams");
        params.add("contextDataParams", contextDataParams);

        return new OvpRequestBuilder().service("baseEntry")
                .action("getContextData")
                .method("POST")
                .url(baseUrl)
                .tag("baseEntry-getContextData")
                .params(params);
    }

    public static OvpRequestBuilder getPlaybackContext(String baseUrl, String ks, String entryId, String referrer) {
        JsonObject params = new JsonObject();
        params.addProperty("entryId", entryId);
        params.addProperty("ks", ks);

        JsonObject contextDataParams = new JsonObject();
        contextDataParams.addProperty("objectType","KalturaContextDataParams");
        if(!TextUtils.isEmpty(referrer)) {
            contextDataParams.addProperty("referrer", referrer);
        }

        params.add("contextDataParams", contextDataParams);

        return new OvpRequestBuilder().service("baseEntry")
                .action("getPlaybackContext")
                .method("POST")
                .url(baseUrl)
                .tag("baseEntry-getPlaybackContext")
                .params(params);
    }




    static class BaseEntryListParams {
        String ks;
        Filter filter;
        ResponseProfile responseProfile;

        public BaseEntryListParams(String ks) {
            this.ks = ks;
            this.filter = new Filter();
            this.responseProfile = new ResponseProfile();
        }

        class Filter {
            String redirectFromEntryId;
        }
    }

}