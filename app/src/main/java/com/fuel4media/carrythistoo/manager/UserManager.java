package com.fuel4media.carrythistoo.manager;

import com.fuel4media.carrythistoo.model.FilterType;
import com.fuel4media.carrythistoo.model.PermitType;
import com.fuel4media.carrythistoo.model.Settings;
import com.fuel4media.carrythistoo.model.State;
import com.fuel4media.carrythistoo.model.User;
import com.fuel4media.carrythistoo.prefrences.AppPreference;

import com.fuel4media.carrythistoo.network.RestClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by shweta on 30/1/18.
 */

public class UserManager {
    private static UserManager _instance;
    private User user;
    private Settings settings;
    private ArrayList<State> states;
    private ArrayList<PermitType> permits;
    private ArrayList<FilterType> filters;

    private UserManager() {
        user = AppPreference.getInstance().getUser();
        settings = AppPreference.getInstance().getSettings();
        states = AppPreference.getInstance().getStates();
        permits = AppPreference.getInstance().getPermits();
        filters = AppPreference.getInstance().getFilters();
    }

    public static UserManager getInstance() {
        if (_instance == null) {
            _instance = new UserManager();
        }
        return _instance;
    }

    public void saveState(ArrayList<State> states) {
        this.states = states;
        AppPreference.getInstance().saveStates(states);
    }

    public void savePermits(ArrayList<PermitType> permits) {
        this.permits = permits;
        AppPreference.getInstance().savePermits(permits);
    }

    public void saveFilters(ArrayList<FilterType> filters) {
        this.filters = filters;
        AppPreference.getInstance().saveFilters(filters);
    }

    public void setUser(User user) {
        this.user = user;
        AppPreference.getInstance().setUser(user);
    }

    public void saveSettings(Settings settings) {
        this.settings = settings;
        AppPreference.getInstance().saveSettings(settings);
    }

    public Settings getSettings() {
        return settings;
    }

    public User getUser() {
        return user;
    }


    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<State> newStates() {
        return new ArrayList<>(states);
    }


    public ArrayList<PermitType> getPermits() {
        return permits;
    }

    public ArrayList<FilterType> getFilters() {
        return filters;
    }


    public void updateUser() {
        AppPreference.getInstance().setUser(user);
    }


    public void logout() {
        AppPreference.getInstance().setLogin(false);
        AppPreference.getInstance().setUser(null);
        AppPreference.getInstance().setSessionToken(null);
        RestClient.getInstance().destroy();
    }

    public void updateSettings(@NotNull Settings settings) {
        if (settings.getCalender_notification() != null) {
            this.settings.setCalender_notification(settings.getCalender_notification());

        } else if (settings.getPush_notification() != null) {
            this.settings.setPush_notification(settings.getPush_notification());

        } else if (settings.getCalender_sync() != null) {
            this.settings.setCalender_sync(settings.getCalender_sync());

        } else if (settings.getGrant_access_app_line() != null) {
            this.settings.setGrant_access_app_line(settings.getGrant_access_app_line());

        } else if (settings.getSms_notification() != null) {
            this.settings.setSms_notification(settings.getSms_notification());

        } else if (settings.getTurn_on_location() != null) {
            this.settings.setTurn_on_location(settings.getTurn_on_location());

        } else if (settings.getCancel_premium() != null) {
            this.settings.setCancel_premium(settings.getCancel_premium());
           /* if (this.settings.getCancel_premium() == 1) {
                setUserType(3);
            } else {
                setUserType(2);
            }*/
        }

        AppPreference.getInstance().saveSettings(this.settings);
    }

    public String getState(String id) {
        if (states != null && id != null) {
            State state1 = new State(id);
            for (State state : states) {
                if (state.equals(state1)) {
                    return state.getState_name();
                }
            }
        }
        return null;
    }

    public State getStateItem(String id) {
        if (states != null && id != null) {
            State state1 = new State(id);
            for (State state : states) {
                if (state.equals(state1)) {
                    return state;
                }
            }
        }
        return null;
    }

    public String getPermitName(String id) {
        if (permits != null && id != null) {
            PermitType permitType = new PermitType(id);
            for (PermitType permit : permits) {
                if (permit.equals(permitType)) {
                    return permit.getPermit_name();
                }
            }
        }
        return null;
    }


    public String getGunZoneName(String id) {
        if (filters != null && id != null) {
            FilterType filterType = new FilterType(id);
            for (FilterType filter : filters) {
                if (filter.equals(filterType)) {
                    return filter.getFilter_name();
                }
            }
        }
        return null;
    }

    public Boolean isFreeium() {
        return user.getUser_type() == 1;
    }

    public Boolean isOnTrial() {
        return !(user.getUser_type() == 3 || user.getUser_type() == 2);
    }

    public void setUserType(int type) {
        user.setUser_type(type);

        AppPreference.getInstance().setUser(this.user);
    }

}

