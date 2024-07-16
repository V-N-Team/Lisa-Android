package ht.lisa.app.network;

import java.util.List;

import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.City;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Poi;
import ht.lisa.app.model.response.AccountResponse;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.model.response.BindDeviceResponse;
import ht.lisa.app.model.response.BolotoDrawResponse;
import ht.lisa.app.model.response.BolotoJackpotResponse;
import ht.lisa.app.model.response.BolotoSubscribeListResponse;
import ht.lisa.app.model.response.ConfirmDeviceResponse;
import ht.lisa.app.model.response.DrawBolotoResponse;
import ht.lisa.app.model.response.DrawLotto3Response;
import ht.lisa.app.model.response.DrawLotto4Response;
import ht.lisa.app.model.response.DrawLotto5Response;
import ht.lisa.app.model.response.DrawLotto5RoyalResponse;
import ht.lisa.app.model.response.DrawLotto5jrResponse;
import ht.lisa.app.model.response.DrawMariageResponse;
import ht.lisa.app.model.response.DrawResponse;
import ht.lisa.app.model.response.JackpotResponse;
import ht.lisa.app.model.response.Lotto3SubscribeListResponse;
import ht.lisa.app.model.response.Lotto5jrJackpotResponse;
import ht.lisa.app.model.response.Lotto5jrSubscribeListResponse;
import ht.lisa.app.model.response.MessageResponse;
import ht.lisa.app.model.response.MonCashRequestResponse;
import ht.lisa.app.model.response.MonCashTransferResponse;
import ht.lisa.app.model.response.PinResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.model.response.Royal5SubscribeListResponse;
import ht.lisa.app.model.response.SogexpressTransferResponse;
import ht.lisa.app.model.response.SubscribeResponse;
import ht.lisa.app.model.response.TicketsPurchaseResponse;
import ht.lisa.app.model.response.TicketsResponse;
import ht.lisa.app.model.response.TransactionHistoryResponse;
import ht.lisa.app.model.response.UserInfoResponse;
import ht.lisa.app.model.response.UserTransferResponse;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

interface LisaApiService {

    @FormUrlEncoded
    @POST("api/device_bind")
    Call<BindDeviceResponse> bindDevice(@Field("phone") String phone, @Field("device_type") String deviceType, @Field("device_id") String deviceId);

    @FormUrlEncoded
    @POST("api/device_confirm")
    Observable<ConfirmDeviceResponse> confirmDevice(@Header("Authorization") String token, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/user_info")
    Call<UserInfoResponse> getUserInfo(@Header("Authorization") String token, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/device_unbind")
    Call<BaseResponse> unbindDevice(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/pin_authorize")
    Call<PinResponse> createPin(@Header("Authorization") String token, @Field("phone") String phone, @Field("pin") String pin);

    @FormUrlEncoded
    @POST("api/pin_authorize")
    Call<PinResponse> authorizePin(@Field("phone") String phone, @Field("pin") String pin);

    @FormUrlEncoded
    @POST("api/pin_change")
    Call<PinResponse> changePin(@Field("phone") String phone, @Field("pin") String currentPin, @Field("new") String newPin);

    @FormUrlEncoded
    @POST("api/user_account")
    Call<AccountResponse> getUserAccount(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/user_profile")
    Call<ProfileResponse> setUserProfile(@Field("phone") String phone, @Field("secret") String secret, @Field("name") String name,
                                         @Field("surname") String surname, @Field("dob") String dob, @Field("sex") String sex, @Field("email") String email, @Field("ig") String ig, @Field("fb") String fb, @Field("city") String city, @Field("avatar") String avatar);

    @FormUrlEncoded
    @POST("api/user_profile")
    Call<ProfileResponse> getUserProfile(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/user_transfer")
    Call<UserTransferResponse> transferUser(@Field("phone") String phone, @Field("recipient") String recipient, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("api/user_transactions")
    Call<TransactionHistoryResponse> getTransactionsHistory(@Field("phone") String phone, @Field("from") int from);

    @GET("api/poi_moncash")
    Call<List<Poi>> getPoiMoncash();

    @GET("api/poi_sogexpress")
    Call<List<Poi>> getPoiSogexpress();

    @GET("api/haitian_cities")
    Call<List<City>> getHaitianCities();

    @FormUrlEncoded
    @POST("api/moncash_transfer")
    Call<MonCashTransferResponse> monCashTransfer(@Field("phone") String phone, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("api/sogexpress_transfer")
    Call<SogexpressTransferResponse> sogexpressTransfer(@Field("phone") String phone, @Field("amount") String amount);

    @POST("api/avatar_list")
    Call<List<Avatar>> getAvatarList();

    @FormUrlEncoded
    @POST("api/moncash_request")
    Call<MonCashRequestResponse> monCashRequest(@Field("phone") String phone, @Field("amount") String amount);

    @FormUrlEncoded
    @POST("api/message_list")
    Call<MessageResponse> getMessageList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/message_list")
    Call<MessageResponse> getMessageList(@Field("phone") String phone, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("api/message_list")
    Call<MessageResponse> getMessageListFrom(@Field("phone") String phone, @Field("from") int lastMessageId, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("api/common_ldn")
    Call<DrawResponse> getDraw(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/common_ldn")
    Call<DrawResponse> getDrawByDate(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/boloto_ldn")
    Call<DrawBolotoResponse> getBolotoDrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/mariage_ldn")
    Call<DrawMariageResponse> getMariageDrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/lotto3_ldn")
    Call<DrawLotto3Response> getLotto3DrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/lotto4_ldn")
    Call<DrawLotto4Response> getLotto4DrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/lotto5_ldn")
    Call<DrawLotto5Response> getLotto5DrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/lotto5jr_ldn")
    Call<DrawLotto5jrResponse> getLotto5jrDrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/royal5_ldn")
    Call<DrawLotto5RoyalResponse> getLotto5RoyalDrawDetail(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/boloto_draw")
    Call<BolotoDrawResponse> getBolotoDraw(@Field("phone") String phone, @Field("draw") int draw);

    @FormUrlEncoded
    @POST("api/user_tickets")
    Call<TicketsResponse> getUserTickets(@Field("phone") String phone, @Field("date") String date);

    @FormUrlEncoded
    @POST("api/user_tickets")
    Call<TicketsResponse> getRecentNumbers(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/boloto_ticket")
    Call<TicketsPurchaseResponse> getBolotoTicket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num);

    @FormUrlEncoded
    @POST("api/royal5_ticket")
    Call<TicketsPurchaseResponse> getRoyal5Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num);

    @FormUrlEncoded
    @POST("api/bolet_ticket")
    Call<TicketsPurchaseResponse> getBoletTicket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost);

    @FormUrlEncoded
    @POST("api/mariage_ticket")
    Call<TicketsPurchaseResponse> getMariageTicket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost);

    @FormUrlEncoded
    @POST("api/lotto3_ticket")
    Call<TicketsPurchaseResponse> getLotto3Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost);

    @FormUrlEncoded
    @POST("api/lotto4_ticket")
    Call<TicketsPurchaseResponse> getLotto4Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost, @Field("type") int type);

    @FormUrlEncoded
    @POST("api/lotto5_ticket")
    Call<TicketsPurchaseResponse> getLotto5Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost, @Field("type") int type);

    @FormUrlEncoded
    @POST("api/lotto4_ticket")
    Call<TicketsPurchaseResponse> getLotto4Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost);

    @FormUrlEncoded
    @POST("api/lotto5_ticket")
    Call<TicketsPurchaseResponse> getLotto5Ticket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num, @Field("cost") int cost);

    @FormUrlEncoded
    @POST("api/lotto5jr_ticket")
    Call<TicketsPurchaseResponse> getLotto5jrTicket(@Field("phone") String phone, @Field("draw") long draw, @Field("num") String num);

    @FormUrlEncoded
    @POST("api/boloto_subscribe_list")
    Call<BolotoSubscribeListResponse> getBolotoSubscribeList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/royal5_subscribe_list")
    Call<Royal5SubscribeListResponse> getRoyal5SubscribeList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/lotto3_subscribe_list")
    Call<Lotto3SubscribeListResponse> getLotto3SubscribeList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/lotto3_subscribe")
    Call<SubscribeResponse> getLotto3Subscribe(@Field("phone") String phone, @Field("num") String num, @Field("cost") int cost, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/lotto5jr_subscribe_list")
    Call<Lotto5jrSubscribeListResponse> getLotto5jrSubscribeList(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/boloto_subscribe")
    Call<SubscribeResponse> getBolotoSubscribe(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/lotto5jr_subscribe")
    Call<SubscribeResponse> getLotto5jrSubscribe(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/royal5_subscribe")
    Call<SubscribeResponse> getRoyal5Subscribe(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @POST("api/boloto_jackpot")
    Call<BolotoJackpotResponse> getBolotoJackpot();

    @POST("api/royal5_jackpot")
    Call<JackpotResponse> getLotto5RoyalJackpot();

    @POST("api/lotto5jr_jackpot")
    Call<Lotto5jrJackpotResponse> getLotto5jrJackpot();

    @FormUrlEncoded
    @POST("api/boloto_unsubscribe")
    Call<BaseResponse> unsubscribeBoloto(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/lotto3_unsubscribe")
    Call<BaseResponse> unsubscribeLotto3(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/lotto5jr_unsubscribe")
    Call<BaseResponse> unsubscribeLotto5jr(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/royal5_unsubscribe")
    Call<BaseResponse> unsubscribeRoyal5(@Field("phone") String phone, @Field("num") String num, @Field("region") String region);

    @FormUrlEncoded
    @POST("api/onesignal_bind")
    Observable<ResponseBody> bindOneSignal(@Field("phone") String phone, @Field("player_id") String userId);

    @FormUrlEncoded
    @POST("api/onesignal_unbind")
    Observable<ResponseBody> unbindOneSignal(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("api/moncash_request_url")
    Call<Message> getReloadMoneyUrl(@Field("phone") String phone, @Field("order_id") String orderId);
}
