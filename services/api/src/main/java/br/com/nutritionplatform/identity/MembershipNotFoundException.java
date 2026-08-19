package br.com.nutritionplatform.identity;

public class MembershipNotFoundException extends RuntimeException {
    public MembershipNotFoundException() {
        super("No active default organization membership was found for this identity.");
    }
}
