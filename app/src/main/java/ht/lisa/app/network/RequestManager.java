package ht.lisa.app.network;

import java.util.List;

import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.City;
import ht.lisa.app.model.Device;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Pin;
import ht.lisa.app.model.PinChange;
import ht.lisa.app.model.Poi;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.User;
import ht.lisa.app.model.UserTransfer;
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
import retrofit2.Callback;

import static ht.lisa.app.util.Settings.TOKEN_MOVILOT;

public class RequestManager extends BaseRequestManager {

    private static RequestManager requestManager;
    private LisaApiService lisaApiService;

    private RequestManager() {
        lisaApiService = LisaApiClient.getApiService();
    }

    public static RequestManager getInstance() {
        if (requestManager == null) {
            requestManager = new RequestManager();
        }

        return requestManager;
    }

    public void bindDevice(Device device, CallbackListener<BindDeviceResponse> callbackListener) {
        lisaApiService.bindDevice(device.getPhone(), device.getDeviceType(), device.getDeviceID()).enqueue(getCallback(callbackListener));
    }

    public Observable<ConfirmDeviceResponse> confirmDevice(String token, Phone phone) {
        return lisaApiService.confirmDevice(TOKEN_MOVILOT + " " + token, phone.getPhone());
    }

    public void getUserInfo(String token, Phone phone, CallbackListener<UserInfoResponse> callbackListener) {
        lisaApiService.getUserInfo(token == null ? null : TOKEN_MOVILOT + " " + token, phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void unbindDevice(Phone phone, CallbackListener<BaseResponse> callbackListener) {
        lisaApiService.unbindDevice(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void createPin(String token, Pin pin, CallbackListener<PinResponse> callbackListener) {
        lisaApiService.createPin(token == null ? null : TOKEN_MOVILOT + " " + token, pin.getPhone(), pin.getPin()).enqueue(getCallback(callbackListener));
    }

    public void authorizePin(Pin pin, CallbackListener<PinResponse> callbackListener) {
        lisaApiService.authorizePin(pin.getPhone(), pin.getPin()).enqueue(getCallback(callbackListener));
    }

    public void changePin(PinChange pinChange, CallbackListener<PinResponse> callbackListener) {
        lisaApiService.changePin(pinChange.getPhone(), pinChange.getCurrentPin(), pinChange.getNewPin()).enqueue(getCallback(callbackListener));
    }

    public void getUserAccount(Phone phone, CallbackListener<AccountResponse> callbackListener) {
        lisaApiService.getUserAccount(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void setUserProfile(User user, CallbackListener<ProfileResponse> callbackListener) {
        lisaApiService.setUserProfile(user.getPhone(), user.getSecret(), user.getName(), user.getSurname(),
                user.getFormattedForServerDob(), user.getSex(), user.getEmail(), user.getIg(), user.getFb(), user.getCity(), user.getAvatar()).enqueue(getCallback(callbackListener));
    }

    public void getUserProfile(Phone phone, CallbackListener<ProfileResponse> callbackListener) {
        lisaApiService.getUserProfile(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void transferUser(UserTransfer userTransfer, CallbackListener<UserTransferResponse> callbackListener) {
        lisaApiService.transferUser(userTransfer.getPhone(), userTransfer.getRecipient(), userTransfer.getAmount()).enqueue(getCallback(callbackListener));
    }

    public void getTransactionsHistory(Phone phone, int from, CallbackListener<TransactionHistoryResponse> callbackListener) {
        lisaApiService.getTransactionsHistory(phone.getPhone(), from).enqueue(getCallback(callbackListener));
    }

    public void getPoiMoncash(Callback<List<Poi>> callbackListener) {
        lisaApiService.getPoiMoncash().enqueue(callbackListener);
    }

    public void getPoiSogexpress(Callback<List<Poi>> callbackListener) {
        lisaApiService.getPoiSogexpress().enqueue(callbackListener);
    }

    public void getHaitianCities(Callback<List<City>> callbackListener) {
        lisaApiService.getHaitianCities().enqueue(callbackListener);
    }

    public void monCashRequest(Phone phone, String amount, CallbackListener<MonCashRequestResponse> callbackListener) {
        lisaApiService.monCashRequest(phone.getPhone(), amount).enqueue(getCallback(callbackListener));
    }

    public void monCashTransfer(Phone phone, String amount, CallbackListener<MonCashTransferResponse> callbackListener) {
        lisaApiService.monCashTransfer(phone.getPhone(), amount).enqueue(getCallback(callbackListener));
    }

    public void sogexpressTransfer(Phone phone, String amount, CallbackListener<SogexpressTransferResponse> callbackListener) {
        lisaApiService.sogexpressTransfer(phone.getPhone(), amount).enqueue(getCallback(callbackListener));
    }

    public void getAvatarList(Callback<List<Avatar>> callbackListener) {
        lisaApiService.getAvatarList().enqueue(callbackListener);
    }

    public void getMessageList(Phone phone, CallbackListener<MessageResponse> callbackListener) {
        lisaApiService.getMessageList(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getMessageList(Phone phone, int limit, CallbackListener<MessageResponse> callbackListener) {
        lisaApiService.getMessageList(phone.getPhone(), limit).enqueue(getCallback(callbackListener));
    }

    public void getMessageListFrom(Phone phone, int lastMessageId, int limit, CallbackListener<MessageResponse> callbackListener) {
        lisaApiService.getMessageListFrom(phone.getPhone(), lastMessageId, limit).enqueue(getCallback(callbackListener));
    }

    public void getDraw(Phone phone, CallbackListener<DrawResponse> callbackListener) {
        lisaApiService.getDraw(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getDrawByDate(Phone phone, String date, CallbackListener<DrawResponse> callbackListener) {
        lisaApiService.getDrawByDate(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getBolotoDrawDetail(Phone phone, String date, CallbackListener<DrawBolotoResponse> callbackListener) {
        lisaApiService.getBolotoDrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getMariageDrawDetail(Phone phone, String date, CallbackListener<DrawMariageResponse> callbackListener) {
        lisaApiService.getMariageDrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getLotto3DrawDetail(Phone phone, String date, CallbackListener<DrawLotto3Response> callbackListener) {
        lisaApiService.getLotto3DrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getLotto4DrawDetail(Phone phone, String date, CallbackListener<DrawLotto4Response> callbackListener) {
        lisaApiService.getLotto4DrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getLotto5DrawDetail(Phone phone, String date, CallbackListener<DrawLotto5Response> callbackListener) {
        lisaApiService.getLotto5DrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getLotto5jrDrawDetail(Phone phone, String date, CallbackListener<DrawLotto5jrResponse> callbackListener) {
        lisaApiService.getLotto5jrDrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getLotto5RoyalDrawDetail(Phone phone, String date, CallbackListener<DrawLotto5RoyalResponse> callbackListener) {
        lisaApiService.getLotto5RoyalDrawDetail(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getBolotoDraw(Phone phone, int draw, CallbackListener<BolotoDrawResponse> callbackListener) {
        lisaApiService.getBolotoDraw(phone.getPhone(), draw).enqueue(getCallback(callbackListener));
    }

    public void getUserTickets(Phone phone, String date, CallbackListener<TicketsResponse> callbackListener) {
        lisaApiService.getUserTickets(phone.getPhone(), date).enqueue(getCallback(callbackListener));
    }

    public void getRecentNumbers(Phone phone, CallbackListener<TicketsResponse> callbackListener) {
        lisaApiService.getRecentNumbers(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getBolotoTicket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getBolotoTicket(phone.getPhone(), ticket.getDraw(), ticket.getNum()).enqueue(getCallback(callbackListener));
    }

    public void getRoyal5Ticket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getRoyal5Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum()).enqueue(getCallback(callbackListener));
    }

    public void getBoletTicket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getBoletTicket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost()).enqueue(getCallback(callbackListener));
    }

    public void getMariageTicket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getMariageTicket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost()).enqueue(getCallback(callbackListener));
    }

    public void getLotto3Ticket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getLotto3Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost()).enqueue(getCallback(callbackListener));
    }

    public void getLotto4Ticket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        if (ticket.getType() != 0) {
            lisaApiService.getLotto4Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost(), ticket.getType()).enqueue(getCallback(callbackListener));
        } else {
            lisaApiService.getLotto4Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost()).enqueue(getCallback(callbackListener));
        }
    }

    public void getLotto5Ticket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        if (ticket.getType() != 0) {
            lisaApiService.getLotto5Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost(), ticket.getType()).enqueue(getCallback(callbackListener));
        } else {
            lisaApiService.getLotto5Ticket(phone.getPhone(), ticket.getDraw(), ticket.getNum(), ticket.getEditedCost()).enqueue(getCallback(callbackListener));
        }
    }

    public void getLotto5jrTicket(Phone phone, Ticket ticket, CallbackListener<TicketsPurchaseResponse> callbackListener) {
        lisaApiService.getLotto5jrTicket(phone.getPhone(), ticket.getDraw(), ticket.getNum()).enqueue(getCallback(callbackListener));
    }

    public void getLotto5jrSubscribeList(Phone phone, CallbackListener<Lotto5jrSubscribeListResponse> callbackListener) {
        lisaApiService.getLotto5jrSubscribeList(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getLotto3SubscribeList(Phone phone, CallbackListener<Lotto3SubscribeListResponse> callbackListener) {
        lisaApiService.getLotto3SubscribeList(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getBolotoSubscribeList(Phone phone, CallbackListener<BolotoSubscribeListResponse> callbackListener) {
        lisaApiService.getBolotoSubscribeList(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getRoyal5SubscribeList(Phone phone, CallbackListener<Royal5SubscribeListResponse> callbackListener) {
        lisaApiService.getRoyal5SubscribeList(phone.getPhone()).enqueue(getCallback(callbackListener));
    }

    public void getBolotoJackpot(CallbackListener<BolotoJackpotResponse> callbackListener) {
        lisaApiService.getBolotoJackpot().enqueue(getCallback(callbackListener));
    }

    public void getLotto5RoyalJackpot(CallbackListener<JackpotResponse> callbackListener) {
        lisaApiService.getLotto5RoyalJackpot().enqueue(getCallback(callbackListener));
    }

    public void getLotto5jrJackpot(CallbackListener<Lotto5jrJackpotResponse> callbackListener) {
        lisaApiService.getLotto5jrJackpot().enqueue(getCallback(callbackListener));
    }

    public void getBolotoSubscribe(Phone phone, String num, String region, CallbackListener<SubscribeResponse> callbackListener) {
        lisaApiService.getBolotoSubscribe(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void getLotto3Subscribe(Phone phone, String num, int cost,  String region, CallbackListener<SubscribeResponse> callbackListener) {
        lisaApiService.getLotto3Subscribe(phone.getPhone(), num, cost, region).enqueue(getCallback(callbackListener));
    }

    public void getLotto5jrSubscribe(Phone phone, String num,  String region, CallbackListener<SubscribeResponse> callbackListener) {
        lisaApiService.getLotto5jrSubscribe(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void getRoyal5Subscribe(Phone phone, String num,  String region, CallbackListener<SubscribeResponse> callbackListener) {
        lisaApiService.getRoyal5Subscribe(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void unsubscribeBoloto(Phone phone, String num, String region, CallbackListener<BaseResponse> callbackListener) {
        lisaApiService.unsubscribeBoloto(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void unsubscribeLotto3(Phone phone, String num, String region, CallbackListener<BaseResponse> callbackListener) {
        lisaApiService.unsubscribeLotto3(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void unsubscribeLotto5jr(Phone phone, String num, String region, CallbackListener<BaseResponse> callbackListener) {
        lisaApiService.unsubscribeLotto5jr(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public void unsubscribeRoyal5(Phone phone, String num, String region, CallbackListener<BaseResponse> callbackListener) {
        lisaApiService.unsubscribeRoyal5(phone.getPhone(), num, region).enqueue(getCallback(callbackListener));
    }

    public Observable<ResponseBody> bindOneSignal(Phone phone, String userId) {
        return lisaApiService.bindOneSignal(phone.getPhone(), userId);
    }

    public Observable<ResponseBody> unbindOneSignal(Phone phone) {
        return lisaApiService.unbindOneSignal(phone.getPhone());
    }

    public void getReloadMoneyUrl(Phone phone, String orderId, CallbackListener<Message> callbackListener) {
        lisaApiService.getReloadMoneyUrl(phone.getPhone(), orderId).enqueue(getCallback(callbackListener));
    }

}
