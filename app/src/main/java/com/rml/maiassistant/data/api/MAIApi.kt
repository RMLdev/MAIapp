package com.rml.maiassistant.data.api

import com.rml.maiassistant.model.SelectionNetworkEntity
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MAIApi {

    @GET("departments/{ApiVersion}?")
    fun getDepartments(
        @Path("ApiVersion") ApiVersion: String,
        @Query("wrapAPIKey") ApiToken: String
    ): Observable<List<SelectionNetworkEntity>>

    @GET("getcollection/{ApiVersion}?")
    fun getGroups(
        @Path("ApiVersion") ApiVersion: String,
        @Query("department") departmentId: Long,
        @Query("course") courseId: Int,
        @Query("wrapAPIKey") ApiToken: String
    ): Observable<List<SelectionNetworkEntity>>

/*    @GET("getschedule/{ApiVersion}?")
    fun getSchedule(
        @Path("ApiVersion") ApiVersion: String,
        @Query("group") group: String,
        @Query("week") week: Int,
        @Query("wrapAPIKey") ApiToken: String
    ): Single<List<ScheduleDay>>*/
}