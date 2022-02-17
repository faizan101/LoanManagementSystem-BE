package com.dtone.lending.util;

import com.dtone.lending.constants.ReportedStatus;
import lombok.Getter;
import lombok.ToString;

public class ResponseUtil {
    @Getter
    @ToString
    public static final class ServiceResponse {
        private final ReportedStatus status;
        private final String description;

        private ServiceResponse(final ReportedStatus status, final String description) {
            this.status = status;
            this.description = description;
        }

        public static ServiceResponse of(final ReportedStatus status, final String body) {
            return new ServiceResponse(status, body);
        }
    }
}
