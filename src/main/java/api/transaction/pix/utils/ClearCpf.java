package api.transaction.pix.utils;

public class ClearCpf {
    public static String clearCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^0-9]", "");
    }
}
