package congdev37.edu.uttedudemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import congdev37.edu.uttedudemo.model.Admin;
import congdev37.edu.uttedudemo.model.Question;
import congdev37.edu.uttedudemo.model.ResponseHistory;
import congdev37.edu.uttedudemo.model.Student;
import congdev37.edu.uttedudemo.model.Subject;
import congdev37.edu.uttedudemo.model.Test;
import congdev37.edu.uttedudemo.model.User;
import congdev37.edu.uttedudemo.response.ResponseMessage;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface SOService {

//        //get
        @GET("getTest.php?getTest&format=json")
        Call<List<Test>> getTest(@QueryMap Map<String, Object> option);

        @GET("getAllQuestion.php?getAllQuestion&format=json")
        Call<List<Question>> getAllQuestion(@QueryMap Map<String, Object> option);

        @GET("getStudent.php?getStudent&format=json")
        Call<List<Student>> getStudent(@QueryMap Map<String, Object> option);

        @GET("getAllAccount.php?getAllAccount&format=json")
        Call<List<User>> getAllAccount(@QueryMap Map<String, Object> option);

        @GET("getAdmin.php?getAdmin&format=json")
        Call<List<Admin>> getAdmin(@QueryMap Map<String, Object> option);
//
        @GET("getAllSubject.php?getAllSubject&format=json")
        Call<List<Subject>> getAllSubject(@QueryMap Map<String, Object> option);

        @GET("getHistory.php?getHistory&format=json")
        Call<ArrayList<ResponseHistory>> getHistory(@QueryMap Map<String, Object> option);

        @GET("getExercise.php?getExercise&format=json")
        Call<ResponseMessage> getExercise(@QueryMap Map<String, Object> option);

        @GET("getQuestion.php?getQuestion&format=json")
        Call<List<Question>> getQuestion(@QueryMap Map<String, Object> option);

        @GET("getUser.php?getUser&format=json")
        Call<ArrayList<User>> getUser(@QueryMap Map<String, Object> option);

        @GET("getSubject.php?getSubject&format=json")
        Call<ArrayList<Subject>> getSubject(@QueryMap Map<String, Object> option);

        //post
        @FormUrlEncoded
        @POST("login.php")
        Call<ResponseMessage> login(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("saveTest.php")
        Call<ResponseMessage> saveTest(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("createSubject.php")
        Call<ResponseMessage> createSubject(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("updateSubject.php")
        Call<ResponseMessage> updateSubject(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("deleteSubject.php")
        Call<ResponseMessage> deleteSubject(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("updateTest.php")
        Call<ResponseMessage> updateTest(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("deleteTest.php")
        Call<ResponseMessage> deleteTest(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("deleteUser.php")
        Call<ResponseMessage> deleteUser(@FieldMap Map<String, Object> params);

        @FormUrlEncoded
        @POST("saveNewTest.php")
        Call<ResponseMessage> saveNewTest(@FieldMap Map<String, Object> params);
//
//        @FormUrlEncoded
//        @POST("createUser.php")
//        Call<ResponseMessage> createUser(@FieldMap Map<String, Object> params);
//
        @FormUrlEncoded
        @POST("updateUser.php")
        Call<ResponseMessage> updateUser(@FieldMap Map<String, Object> params);

//        @FormUrlEncoded
//        @POST("updateTest.php")
//        Call<ResponseMessage> updateTest(@FieldMap Map<String, Object> params);
//
//        @FormUrlEncoded
//        @POST("savePoint.php")
//        Call<ResponseMessage> savePoint(@FieldMap Map<String, Object> params);

}
