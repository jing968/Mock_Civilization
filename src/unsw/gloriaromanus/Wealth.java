package unsw.gloriaromanus;

import java.io.Serializable;

public class Wealth implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 9021355360954337972L;
    private Province province;
    private double wealth;
    private String taxRate;
    private double rate;

    public Wealth(Province province) {
        this.province = province;
        this.wealth = 0;
        this.taxRate = "Low tax";
        this.rate = 0.1;
    }

    /********** Wealth ************/
    public double getWealth() {
        return wealth;
    }

    public void addWealth(double wealth) {
        this.wealth += wealth;
    }

    /**
     * Get associated wealth with taxRate.
     * @return wealth.
     */
    public double getWealthTaxRate() {
        if (taxRate.equals("Low tax")) return 10;
        else if (taxRate.equals("Normal tax"))  return 0;
        else if (taxRate.equals("High tax")) return -10;
        else return -30; // High tax rate.
    }

    /********** Tax Rate ************/
    /**
     * Sets the tax on valid taxRate string.
     * @param taxRate
     */
    public String setTaxRate(String taxRate) {
        if (taxRate.equals("Low tax")) return setRateAndTaxRate(taxRate, 0.1);
        else if (taxRate.equals("Normal tax")) return setRateAndTaxRate(taxRate, 0.15);
        else if (taxRate.equals("High tax")) return setRateAndTaxRate(taxRate, 0.2);
        else if (taxRate.equals("Very high tax")) return setRateAndTaxRate(taxRate, 0.25);
        else return "Invalid Tax";
    }

    public String getTaxRate() {
        return taxRate;
    }

    /********** Rate ************/
    /**
     * Change both taxRate and rate at the same time.
     * @param taxRate
     * @param rate
     */
    public String setRateAndTaxRate(String taxRate, double rate) {
        this.taxRate = taxRate;
        this.rate = rate;
        return "Successfully Set New Tax";
    }

    public double getRate() {
        return rate;
    }

    /**
     * Adds/Subtract wealth, gold and faction wealth depending on 
     *  type of tax this province is under.
     * If under very high tax, all units morale will be reduced by 1.
     */
    public void generateWealth() {
        if (taxRate.equals("Very high tax")) province.reduceMorale();
        addWealth(getWealthTaxRate());
        double goldCut = getWealth() * getRate();
        province.setGold(goldCut);
        province.addWealthToFaction(getWealthTaxRate());
    }
}
