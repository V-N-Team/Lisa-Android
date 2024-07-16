package ht.lisa.app.ui.wallet;

import androidx.annotation.NonNull;

import java.util.List;

import ht.lisa.app.LisaApp;
import ht.lisa.app.model.Avatar;
import ht.lisa.app.model.City;
import ht.lisa.app.model.Message;
import ht.lisa.app.model.Phone;
import ht.lisa.app.model.Pin;
import ht.lisa.app.model.Poi;
import ht.lisa.app.model.Ticket;
import ht.lisa.app.model.User;
import ht.lisa.app.model.UserTransfer;
import ht.lisa.app.model.response.AccountResponse;
import ht.lisa.app.model.response.AvatarResponse;
import ht.lisa.app.model.response.BaseResponse;
import ht.lisa.app.model.response.BolotoDrawResponse;
import ht.lisa.app.model.response.BolotoJackpotResponse;
import ht.lisa.app.model.response.BolotoSubscribeListResponse;
import ht.lisa.app.model.response.CitiesResponse;
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
import ht.lisa.app.model.response.PoiMonCashResponse;
import ht.lisa.app.model.response.PoiSogexpressResponse;
import ht.lisa.app.model.response.ProfileResponse;
import ht.lisa.app.model.response.Royal5SubscribeListResponse;
import ht.lisa.app.model.response.SogexpressTransferResponse;
import ht.lisa.app.model.response.SubscribeResponse;
import ht.lisa.app.model.response.TicketsPurchaseResponse;
import ht.lisa.app.model.response.TicketsResponse;
import ht.lisa.app.model.response.TransactionHistoryResponse;
import ht.lisa.app.model.response.UserTransferResponse;
import ht.lisa.app.network.CallbackListener;
import ht.lisa.app.network.RequestManager;
import ht.lisa.app.ui.component.PinView;
import ht.lisa.app.ui.mvp.BasePresenter;
import ht.lisa.app.util.SharedPreferencesUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletPresenter extends BasePresenter<WalletContract.View> {

    private RequestManager requestManager = RequestManager.getInstance();

    public void transferUser(UserTransfer userTransfer) {
        getView().showProgress();
        requestManager.transferUser(userTransfer, new CallbackListener<UserTransferResponse>() {
            @Override
            public void onSuccess(UserTransferResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                if (response == null) return;
                if (response.getState() != 0) {
                    getView().showMessage(response.getErrorMessage());
                }
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    void authorizePin(Pin pin, PinView pinView) {
        getView().showProgress();
        requestManager.authorizePin(pin, new CallbackListener<PinResponse>() {
            @Override
            public void onSuccess(PinResponse response) {
                getView().hideProgress();
                if (response == null) return;
                if (response.getState() == 0 || response.getState() == -1) {
                    getView().getData(response);
                } else {
                    getView().showMessage(response.getErrorMessage());
                }
                pinView.clear();
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getProfile(Phone phone) {
        getView().showProgress();
        requestManager.getUserProfile(phone, new CallbackListener<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse response) {
                if (response.getState() == 0) {
                    User user1 = response.getUser();
                    user1.setPhone(SharedPreferencesUtil.getPhone());
                    LisaApp.getInstance().setUser(user1);
                    LisaApp.getInstance().setReadonlyFields(response.getReadonly());
                }
                if (getView() == null) return;
                getView().getData(response);
                getView().hideProgress();
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void editProfile(User user) {
        getView().showProgress();
        requestManager.setUserProfile(user, new CallbackListener<ProfileResponse>() {
            @Override
            public void onSuccess(ProfileResponse response) {
                getView().hideProgress();
                if (response.getState() == 0) {
                    //    Log.d("userTAG", "onResponse " + response.getName());

                    User user1 = response.getUser();
                    user1.setPhone(SharedPreferencesUtil.getPhone());
                    LisaApp.getInstance().setUser(user1);
                    LisaApp.getInstance().setReadonlyFields(response.getReadonly());
                    /*

                    LisaApp.getInstance().setUser(new User(SharedPreferencesUtil.getPhone(), response.getSecret(), response.getName(), response.getSurname(),
                            response.getDob(), response.getSex(), response.getEmail(), response.getIg(), response.getFb(), response.getCity(), response.getAvatar()));
            */
                    if (getView() == null) return;
                    getView().getData(response);
                } else if (response.getState() == 5) {
                    getView().showMessage(response.getBaseErrorMessage());
                }
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getAccount(Phone phone) {
        getView().showProgress();
        requestManager.getUserAccount(phone, new CallbackListener<AccountResponse>() {
            @Override
            public void onSuccess(AccountResponse response) {
                if (getView() != null) {
                    getView().hideProgress();
                    getView().getData(response);
                }
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getTransactionsHistory(Phone phone, int from) {
        getView().showProgress();
        requestManager.getTransactionsHistory(phone, from, new CallbackListener<TransactionHistoryResponse>() {
            @Override
            public void onSuccess(TransactionHistoryResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getPoiMoncash() {
        getView().showProgress();
        requestManager.getPoiMoncash(new Callback<List<Poi>>() {
            @Override
            public void onResponse(@NonNull Call<List<Poi>> call, @NonNull Response<List<Poi>> response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(new PoiMonCashResponse(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Poi>> call, @NonNull Throwable t) {
                onFailureAction(t);
            }
        });
    }

    public void getPoiSogexpress() {
        getView().showProgress();
        requestManager.getPoiSogexpress(new Callback<List<Poi>>() {
            @Override
            public void onResponse(@NonNull Call<List<Poi>> call, @NonNull Response<List<Poi>> response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(new PoiSogexpressResponse(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Poi>> call, @NonNull Throwable t) {
                onFailureAction(t);
            }
        });
    }

    public void getHaitianCities() {
        getView().showProgress();
        requestManager.getHaitianCities(new Callback<List<City>>() {
            @Override
            public void onResponse(@NonNull Call<List<City>> call, @NonNull Response<List<City>> response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(new CitiesResponse(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<City>> call, @NonNull Throwable t) {
                onFailureAction(t);

            }
        });
    }

    public void monCashRequest(Phone phone, String amount) {
        getView().showProgress();
        requestManager.monCashRequest(phone, amount, new CallbackListener<MonCashRequestResponse>() {
            @Override
            public void onSuccess(MonCashRequestResponse response) {
                if (getView() == null) return;
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void monCashTransfer(Phone phone, String amount) {
        getView().showProgress();
        requestManager.monCashTransfer(phone, amount, new CallbackListener<MonCashTransferResponse>() {
            @Override
            public void onSuccess(MonCashTransferResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void sogexpressTransfer(Phone phone, String amount) {
        getView().showProgress();
        requestManager.sogexpressTransfer(phone, amount, new CallbackListener<SogexpressTransferResponse>() {
            @Override
            public void onSuccess(SogexpressTransferResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getAvatarList() {
        getView().showProgress();
        requestManager.getAvatarList(new Callback<List<Avatar>>() {
            @Override
            public void onResponse(@NonNull Call<List<Avatar>> call, @NonNull Response<List<Avatar>> response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(new AvatarResponse(response.body()));
            }

            @Override
            public void onFailure(@NonNull Call<List<Avatar>> call, @NonNull Throwable t) {
                onFailureAction(t);
            }
        });
    }

    public void getDraw(Phone phone) {
        getView().showProgress();
        requestManager.getDraw(phone, new CallbackListener<DrawResponse>() {
            @Override
            public void onSuccess(DrawResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getDrawByDate(Phone phone, String date) {
        getView().showProgress();
        requestManager.getDrawByDate(phone, date, new CallbackListener<DrawResponse>() {
            @Override
            public void onSuccess(DrawResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoDrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getBolotoDrawDetail(phone, date, new CallbackListener<DrawBolotoResponse>() {
            @Override
            public void onSuccess(DrawBolotoResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getMariageDrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getMariageDrawDetail(phone, date, new CallbackListener<DrawMariageResponse>() {
            @Override
            public void onSuccess(DrawMariageResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto3DrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getLotto3DrawDetail(phone, date, new CallbackListener<DrawLotto3Response>() {
            @Override
            public void onSuccess(DrawLotto3Response response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto4DrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getLotto4DrawDetail(phone, date, new CallbackListener<DrawLotto4Response>() {
            @Override
            public void onSuccess(DrawLotto4Response response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5DrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getLotto5DrawDetail(phone, date, new CallbackListener<DrawLotto5Response>() {
            @Override
            public void onSuccess(DrawLotto5Response response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5jrDrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getLotto5jrDrawDetail(phone, date, new CallbackListener<DrawLotto5jrResponse>() {
            @Override
            public void onSuccess(DrawLotto5jrResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5RoyalDrawDetail(Phone phone, String date) {
        getView().showProgress();
        requestManager.getLotto5RoyalDrawDetail(phone, date, new CallbackListener<DrawLotto5RoyalResponse>() {
            @Override
            public void onSuccess(DrawLotto5RoyalResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getMessageList(Phone phone) {
        getView().showProgress();
        requestManager.getMessageList(phone, new CallbackListener<MessageResponse>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getMessageList(Phone phone, int limit) {
        getView().showProgress();
        requestManager.getMessageList(phone, limit, new CallbackListener<MessageResponse>() {
            @Override
            public void onSuccess(MessageResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoDraw(Phone phone, int draw) {
        getView().showProgress();
        requestManager.getBolotoDraw(phone, draw, new CallbackListener<BolotoDrawResponse>() {
            @Override
            public void onSuccess(BolotoDrawResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getUserTickets(Phone phone, String date) {
        getView().showProgress();
        requestManager.getUserTickets(phone, date, new CallbackListener<TicketsResponse>() {
            @Override
            public void onSuccess(TicketsResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getRecentNumbers(Phone phone) {
        getView().showProgress();
        requestManager.getRecentNumbers(phone, new CallbackListener<TicketsResponse>() {
            @Override
            public void onSuccess(TicketsResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoTicket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getBolotoTicket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBoletTicket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getBoletTicket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getMariageTicket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getMariageTicket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto3Ticket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getLotto3Ticket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto4Ticket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getLotto4Ticket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5Ticket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getLotto5Ticket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5RoyalTicket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getRoyal5Ticket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5jrTicket(Phone phone, Ticket ticket) {
        getView().showProgress();
        requestManager.getLotto5jrTicket(phone, ticket, new CallbackListener<TicketsPurchaseResponse>() {
            @Override
            public void onSuccess(TicketsPurchaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoSubscribeList(Phone phone) {
        getView().showProgress();
        requestManager.getBolotoSubscribeList(phone, new CallbackListener<BolotoSubscribeListResponse>() {
            @Override
            public void onSuccess(BolotoSubscribeListResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getRoyal5SubscribeList(Phone phone) {
        getView().showProgress();
        requestManager.getRoyal5SubscribeList(phone, new CallbackListener<Royal5SubscribeListResponse>() {
            @Override
            public void onSuccess(Royal5SubscribeListResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5jrSubscribeList(Phone phone) {
        getView().showProgress();
        requestManager.getLotto5jrSubscribeList(phone, new CallbackListener<Lotto5jrSubscribeListResponse>() {
            @Override
            public void onSuccess(Lotto5jrSubscribeListResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto3SubscribeList(Phone phone) {
        getView().showProgress();
        requestManager.getLotto3SubscribeList(phone, new CallbackListener<Lotto3SubscribeListResponse>() {
            @Override
            public void onSuccess(Lotto3SubscribeListResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoJackpot() {
        getView().showProgress();
        requestManager.getBolotoJackpot(new CallbackListener<BolotoJackpotResponse>() {
            @Override
            public void onSuccess(BolotoJackpotResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5RoyalJackpot() {
        getView().showProgress();
        requestManager.getLotto5RoyalJackpot(new CallbackListener<JackpotResponse>() {
            @Override
            public void onSuccess(JackpotResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5jrJackpot() {
        getView().showProgress();
        requestManager.getLotto5jrJackpot(new CallbackListener<Lotto5jrJackpotResponse>() {
            @Override
            public void onSuccess(Lotto5jrJackpotResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getBolotoSubscribe(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.getBolotoSubscribe(phone, num, region, new CallbackListener<SubscribeResponse>() {
            @Override
            public void onSuccess(SubscribeResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto5jrSubscribe(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.getLotto5jrSubscribe(phone, num, region, new CallbackListener<SubscribeResponse>() {
            @Override
            public void onSuccess(SubscribeResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getRoyal5Subscribe(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.getRoyal5Subscribe(phone, num, region, new CallbackListener<SubscribeResponse>() {
            @Override
            public void onSuccess(SubscribeResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getLotto3Subscribe(Phone phone, String num, String region, int cost) {
        getView().showProgress();
        requestManager.getLotto3Subscribe(phone, num, cost * 100, region, new CallbackListener<SubscribeResponse>() {
            @Override
            public void onSuccess(SubscribeResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void unsubscribeBoloto(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.unsubscribeBoloto(phone, num, region, new CallbackListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void unsubscribeLotto3(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.unsubscribeLotto3(phone, num, region, new CallbackListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void unsubscribeLotto5jr(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.unsubscribeLotto5jr(phone, num, region, new CallbackListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }


    public void unsubscribeRoyal5(Phone phone, String num, String region) {
        getView().showProgress();
        requestManager.unsubscribeRoyal5(phone, num, region, new CallbackListener<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    public void getReloadMoneyUrl(Phone phone, String orderId) {
        getView().showProgress();
        requestManager.getReloadMoneyUrl(phone, orderId, new CallbackListener<Message>() {
            @Override
            public void onSuccess(Message response) {
                if (getView() == null) return;
                getView().hideProgress();
                getView().getData(response);
            }

            @Override
            public void onFailure(Throwable error) {
                onFailureAction(error);
            }
        });
    }

    private void onFailureAction(Throwable error) {
        if (getView() == null) return;
        getView().hideProgress();
        getView().showMessage(error.getLocalizedMessage());
        error.printStackTrace();
    }
}
