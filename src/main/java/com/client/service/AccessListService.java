package com.client.service;

import com.client.domain.db.AccessList;
import com.client.domain.enums.AccessType;
import com.client.repository.AccessListRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sdaskaliesku
 */
@Service
public class AccessListService extends AbstractService<AccessList> {

    public List<AccessList> getAllClans() {
        return getRepository().getAllClansOrUsers(true);
    }

    public List<AccessList> getAllUsers() {
        return getRepository().getAllClansOrUsers(false);
    }

    public AccessList getAccessByClanOrUserName(String userName, String clanName) {
        AccessList result = new AccessList();
        List<AccessList> list = getRepository().getAccessByClanOrUserName(userName, clanName);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        result.setAccessType(getBestAccessType(list));
        result.setDueDate(getLastDate(list));
        return result;
    }

    private Date getLastDate(List<AccessList> lists) {
        List<Date> dates = new ArrayList<>();
        for (AccessList accessList : lists) {
            dates.add(accessList.getDueDate());
        }
        // default is asc sort, max is better in this case
        Collections.sort(dates);
        return dates.get(dates.size() - 1);
    }

    private AccessType getBestAccessType(List<AccessList> lists) {
        List<Integer> ordinalList = new ArrayList<>();
        for (AccessList accessList : lists) {
            ordinalList.add(accessList.getAccessType().ordinal());
        }
        // default is asc sort, minimal is better in this case
        Collections.sort(ordinalList);
        int ordinal = ordinalList.get(0);
        return AccessType.values()[ordinal];
    }

    private AccessListRepository getRepository() {
        return (AccessListRepository) repository;
    }

}
