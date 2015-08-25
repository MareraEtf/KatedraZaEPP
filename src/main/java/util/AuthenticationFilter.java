/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import db.Korisnik;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Marko
 */
public class AuthenticationFilter implements Filter {

    private FilterConfig config;
//    private final String[] adminList = new String[]{"odobravanjeKorisnika"};
    private static Map<String, int[]> lista = new HashMap<String, int[]>();

    static {
        lista.put("index_ulogovan.xhtml", new int[]{1, 2, 3, 4});
        lista.put("dodavanjeObavestenja.xhtml", new int[]{1, 3, 4});
        lista.put("promenaPodataka.xhtml", new int[]{1});
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        if (((HttpServletRequest) req).getSession().getAttribute("korisnik") == null) {
            ((HttpServletResponse) resp).sendRedirect("../error.xhtml");
        } else {
            Korisnik korisnik = (Korisnik) ((HttpServletRequest) req).getSession().getAttribute("korisnik");
            System.out.println(korisnik.getKorisnicko_ime());
            String url = ((HttpServletRequest) req).getRequestURL().toString();
            String[] parts = url.split("/");
            int[] level = lista.get(parts[parts.length - 1]);

            if (level != null) {
                if (isInList(level, korisnik.getTip())) {
                    chain.doFilter(req, resp);
                    return;
                }
            }

            ((HttpServletResponse) resp).sendRedirect("../error.xhtml");
        }

    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
        config = null;
    }

    private boolean isInList(int[] list, int val) {

        for (int i : list) {
            if (i == val) {
                return true;
            }
        }

        return false;
    }
}
