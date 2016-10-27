/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.pdsw.samples.services;

import edu.eci.pdsw.samples.entities.Comentario;
import edu.eci.pdsw.samples.entities.EntradaForo;
import edu.eci.pdsw.samples.entities.Usuario;
import edu.eci.pdsw.samples.persistence.DaoEntradaForo;
import edu.eci.pdsw.samples.persistence.DaoFactory;
import edu.eci.pdsw.samples.persistence.DaoUsuario;
import edu.eci.pdsw.samples.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author 2105537
 */


public class ServiciosForoDAOStub extends ServiciosForo implements Serializable{

    private final DaoFactory daof;
    private final Properties properties;

    public ServiciosForoDAOStub() {
        this.properties = new PropertiesLoader().readProperties("applicationconfig.properties");
        this.daof=DaoFactory.getInstance(properties);
    }

    @Override
    public void agregarUsuario(String correo,Usuario us){
        try {
            daof.beginSession();
            if(daof.getDaoUsuario().load(correo)==null){
                daof.getDaoUsuario().save(us);
            }
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<EntradaForo> consultarEntradasForo() throws ExcepcionServiciosForos {
        List<EntradaForo> temp;
        try {
            daof.beginSession();
            temp=daof.getDaoEntradaForo().loadAll();
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosForos ("",ex);
        }
        return temp;
    }

    @Override
    public EntradaForo consultarEntradaForo(int id) throws ExcepcionServiciosForos {
        EntradaForo temp = null;
        try {
            daof.beginSession();
            temp=daof.getDaoEntradaForo().load(id);
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

    
    @Override
    public void registrarNuevaEntradaForo(EntradaForo f1) throws ExcepcionServiciosForos {
        try {
            daof.beginSession();
            daof.getDaoEntradaForo().save(f1);
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void agregarRespuestaForo(int idforo, Comentario c) throws ExcepcionServiciosForos {
        try {
            daof.beginSession();
            daof.getDaoEntradaForo().addToForo(idforo, c);
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Usuario consultarUsuario(String email) throws ExcepcionServiciosForos {
        Usuario temp=null;
        try {
            daof.beginSession();
            temp=daof.getDaoUsuario().load(email);
            daof.commitTransaction();
            daof.endSession();
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiciosForoDAOStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return temp;
    }

        
    }
class PropertiesLoader {

    public Properties readProperties(String fileName){
        InputStream input = null;
        Properties properties = new Properties();
        input = this.getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
}
