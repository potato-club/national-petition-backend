package com.example.nationalpetition.dto.notification.response;

import java.util.Comparator;

public class NotificationInfoResponseComparator implements Comparator<NotificationInfoResponse> {
    @Override
    public int compare(NotificationInfoResponse n1, NotificationInfoResponse n2) {
        if (n1.getId() > n2.getId()) {
            return 1;
        } else if (n1.getId() < n2.getId()) {
            return -1;
        }
        return 0;
    }
}
