package Model;

import java.math.BigDecimal;
import java.util.Map;

public record RequestQuota(String result,
                           String documentation,
                           String terms_of_use,
                           int plan_quota,
                           int requests_remaining,
                           int refresh_day_of_month) {
}
