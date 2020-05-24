
package org.myproject.parking.pricing;

import org.junit.Before;
import org.junit.Test;
import org.myproject.parking.pricing.policy.FixedPricePlusPolicy;
import org.myproject.parking.pricing.policy.StandardPricingPolicy;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;


public class PricingPolicyCatalogTest {

    private PricingPolicyCatalog pricingPolicyCatalog;

    private PricingPolicy pricingPolicy = new StandardPricingPolicy();

    @Before
    public void init() {
        List<PricingPolicy> policyList = new LinkedList<>();
        policyList.add(pricingPolicy);
        pricingPolicyCatalog = new PricingPolicyCatalog(policyList);
    }

    @Test
    public void checkConstructorTest() {
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.STANDARD), is(pricingPolicy));
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.FIXED_PLUS), is(nullValue()));
    }

    @Test
    public void registerPolicyTest() {
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.FIXED_PLUS), is(nullValue()));
        PricingPolicy fixedPlusPolicy = new FixedPricePlusPolicy();
        pricingPolicyCatalog.registerPolicy(PolicyType.FIXED_PLUS, fixedPlusPolicy);
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.FIXED_PLUS), is(fixedPlusPolicy));
    }

    @Test
    public void removeClientByTypeTest() {
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.STANDARD), is(pricingPolicy));
        pricingPolicyCatalog.removePolicyByType(PolicyType.STANDARD);
        assertThat(pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.FIXED_PLUS), is(nullValue()));
    }

    @Test
    public void getPolicyByPolicyTypeTest() {
        PricingPolicy pricingPolicyCur = pricingPolicyCatalog.getPolicyByPolicyType(PolicyType.STANDARD);
        assertThat(pricingPolicyCur, is(pricingPolicy));
        assertThat(pricingPolicyCur.getClass(), is(StandardPricingPolicy.class));
    }

    @Test
    public void getPolicyByPolicyTypeAsStringTest() {
        String pollTypePing = "STANDARD";
        String pollTypeJmx = "FIXED_PLUS";

        PricingPolicy pricingPolicyStandard = pricingPolicyCatalog.getPolicyByPolicyTypeAsString(pollTypePing);
        assertThat(pricingPolicyStandard, is(pricingPolicy));

        PricingPolicy pricingPolicyPlus = new FixedPricePlusPolicy();
        pricingPolicyCatalog.registerPolicy(PolicyType.FIXED_PLUS, pricingPolicyPlus);

        PricingPolicy pricingPolicyJmxReturned = pricingPolicyCatalog.getPolicyByPolicyTypeAsString(pollTypeJmx);
        assertThat(pricingPolicyPlus, is(pricingPolicyJmxReturned));
    }
}
