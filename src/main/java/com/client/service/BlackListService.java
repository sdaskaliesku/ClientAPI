package com.client.service;

import com.client.domain.db.BlackList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author sdaskaliesku
 */
@Service
public class BlackListService extends AbstractService<BlackList> {

    public boolean isUserInBlackList(String user) {
        List<BlackList> blackList = read();
        for (BlackList list : blackList) {
            if (!list.getClan()) {
                if (list.getNickOrClanName().equalsIgnoreCase(user)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isClanInBlackList(String clan) {
        List<BlackList> blackList = read();
        for (BlackList list : blackList) {
            if (list.getClan()) {
                if (list.getNickOrClanName().equalsIgnoreCase(clan)) {
                    return true;
                }
            }
        }
        return false;
    }
}
