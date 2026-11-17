package br.edu.bd2.loja.app;

import br.edu.bd2.loja.api.ApiServer;
import br.edu.bd2.loja.carga.CargaDados;
import br.edu.bd2.loja.console.ConsoleApp;
import br.edu.bd2.loja.dao.JPAUtil;

public class Main {

    public static void main(String[] args) throws Exception {
        String comando = args.length > 0 ? args[0] : "console";
        switch (comando) {
            case "console":
                new ConsoleApp().executar();
                JPAUtil.fechar();
                break;
            case "api":
                ApiServer.iniciar(Integer.parseInt(System.getenv().getOrDefault("PORT", "8080")));
                break;
            case "carga":
                CargaDados carga = new CargaDados();
                if (carga.jaCarregado()) {
                    System.out.println("O banco já possui clientes; carga ignorada. Para recriar: docker compose down -v");
                } else {
                    carga.executar();
                }
                JPAUtil.fechar();
                break;
            case "ddl":
                String destino = args.length > 1 ? args[1] : "sql/01_ddl_oltp.sql";
                JPAUtil.gerarDDL(destino);
                System.out.println("DDL gerado em " + destino);
                JPAUtil.fechar();
                break;
            default:
                System.out.println("Comando desconhecido: " + comando + ". Use console, api, carga ou ddl.");
                System.exit(1);
        }
    }
}
