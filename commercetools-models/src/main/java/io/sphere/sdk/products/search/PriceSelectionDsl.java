package io.sphere.sdk.products.search;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Referenceable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import java.util.List;

public final class PriceSelectionDsl extends Base implements PriceSelection {
    @Nonnull
    private final String priceCurrency;
    @Nullable
    private final String priceCountry;
    @Nullable
    private final String priceCustomerGroup;
    @Nullable
    private final String priceChannel;
    @Nullable
    private final String storeProjection;
    @Nullable
    private final List<String> localeProjection;

    PriceSelectionDsl(@Nonnull final String priceCurrency, final String priceCountry, final String priceCustomerGroup, final String priceChannel, final String storeProjection, final List<String> localeProjection) {
        this.priceChannel = priceChannel;
        this.priceCurrency = priceCurrency;
        this.priceCountry = priceCountry;
        this.priceCustomerGroup = priceCustomerGroup;
        this.storeProjection = storeProjection;
        this.localeProjection = localeProjection;
    }

    public static PriceSelectionDsl ofCurrencyCode(final String currencyCode) {
        return PriceSelectionBuilder.ofCurrencyCode(currencyCode).build();
    }

    public static PriceSelectionDsl of(@Nonnull final CurrencyUnit currencyUnit) {
        return ofCurrencyCode(currencyUnit.getCurrencyCode());
    }

    private PriceSelectionBuilder newBuilder() {
        return PriceSelectionBuilder.of(this);
    }

    public PriceSelectionDsl withPriceCurrencyCode(@Nonnull final String priceCurrencyCode) {
        return newBuilder().priceCurrencyCode(priceCurrencyCode).build();
    }

    public PriceSelectionDsl withPriceCurrency(@Nonnull final CurrencyUnit currencyUnit) {
        return withPriceCurrencyCode(currencyUnit.getCurrencyCode());
    }

    public PriceSelectionDsl withPriceCountryCode(@Nullable final String priceCountryCode) {
        return newBuilder().priceCountryCode(priceCountryCode).build();
    }

    public PriceSelectionDsl withPriceCountry(@Nullable final CountryCode priceCountry) {
        return newBuilder().priceCountry(priceCountry).build();
    }

    public PriceSelectionDsl withPriceCustomerGroup(@Nullable final Referenceable<CustomerGroup> priceCustomerGroup) {
        return newBuilder().priceCustomerGroup(priceCustomerGroup).build();
    }

    public PriceSelectionDsl withPriceCustomerGroupId(@Nullable final String priceCustomerGroupId) {
        return newBuilder().priceCustomerGroupId(priceCustomerGroupId).build();
    }

    public PriceSelectionDsl withPriceChannelId(@Nullable final String priceChannelId) {
        return newBuilder().priceChannelId(priceChannelId).build();
    }

    public PriceSelectionDsl withPriceChannel(@Nullable final Referenceable<Channel> priceChannel) {
        return newBuilder().priceChannel(priceChannel).build();
    }

    public PriceSelectionDsl withStoreProjection(@Nullable final String storeProjection) {
        return newBuilder().storeProjection(storeProjection).build();
    }

    public PriceSelectionDsl withLocaleProjection(@Nullable final List<String> localeProjection) {
        return newBuilder().localeProjection(localeProjection).build();
    }

    public PriceSelectionDsl withPlusLocaleProjection(@Nullable final String localeProjection) {
        return newBuilder().plusLocaleProjection(localeProjection).build();
    }

    @Override
    @Nullable
    public String getPriceChannel() {
        return priceChannel;
    }

    @Override
    @Nullable
    public String getPriceCountry() {
        return priceCountry;
    }

    @Override
    @Nonnull
    public String getPriceCurrency() {
        return priceCurrency;
    }

    @Override
    @Nullable
    public String getPriceCustomerGroup() {
        return priceCustomerGroup;
    }

    @Override
    @Nullable
    public String getStoreProjection() {
        return storeProjection;
    }

    @Override
    @Nullable
    public List<String> getLocaleProjection() {
        return localeProjection;
    }
}
