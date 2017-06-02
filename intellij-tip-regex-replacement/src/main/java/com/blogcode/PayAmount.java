package com.blogcode;

/**
 * Created by jojoldu@gmail.com on 2017. 6. 1.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public class PayAmount {
    private long originAmount;
    private long supplyAmount;
    private long vat;

    public long getOriginAmount() {
        return originAmount;
    }

    public long getSupplyAmount() {
        return supplyAmount;
    }

    public long getVat() {
        return vat;
    }

    public static final class Builder {
        private long originAmount;
        private long supplyAmount;
        private long vat;

        private Builder() {}

        public static Builder builder() {
            return new Builder();
        }

        public Builder originAmount(long originAmount) {
            this.originAmount = originAmount;
            return this;
        }

        public Builder supplyAmount(long supplyAmount) {
            this.supplyAmount = supplyAmount;
            return this;
        }

        public Builder vat(long vat) {
            this.vat = vat;
            return this;
        }

        public PayAmount build() {
            PayAmount payAmount = new PayAmount();
            payAmount.vat = this.vat;
            payAmount.originAmount = this.originAmount;
            payAmount.supplyAmount = this.supplyAmount;
            return payAmount;
        }
    }
}
