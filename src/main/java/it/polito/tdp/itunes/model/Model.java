package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	//punto 1
	private Graph<Album, DefaultEdge>grafo;
	private List<Album>allAlbum;
	private ItunesDAO dao;
	private Map<Integer, Album>idMap;
	//punto 2
	private List<Album>migliore;
	private int nAlbumMax; 
	
	public Model() {
		this.allAlbum = new ArrayList<>();
		this.dao = new ItunesDAO();
		this.idMap = new HashMap<>();
	}
	
	public String creaGrafo(double durataMIN) {
		double durataMS = durataMIN*60000;
		//grafo
		this.grafo = new SimpleGraph<Album, DefaultEdge>(DefaultEdge.class);
		//vertici
		this.allAlbum = dao.getAllAlbums(durataMS);
		Graphs.addAllVertices(grafo, allAlbum);
		//idMap
		for(Album a : this.allAlbum) {
			this.idMap.put(a.getAlbumId(), a);
		}
		//archi
		List<CoppiaA>archi = new ArrayList<>(dao.getAllCoppie(idMap));
		for(CoppiaA x : archi) {
			if(this.idMap.containsValue(x.getA1()) && this.idMap.containsValue(x.getA2()))
				this.grafo.addEdge(x.getA1(), x.getA2());
		}
		return"Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	public int numeroComponenteConnessa(Album a) {
			int nComponentiConnesse = 0;
			ConnectivityInspector<Album, DefaultEdge> inspector = new ConnectivityInspector<>(this.grafo);
	        Set<Album> connectedComponents = inspector.connectedSetOf(a);
	                  for (Album x : connectedComponents) {
	                 	    nComponentiConnesse++;
		         }
	        return nComponentiConnesse;
	}
	
	public double durataComponenteConnessa(Album a) {
		double durataTOT = 0;
		ConnectivityInspector<Album, DefaultEdge> inspector = new ConnectivityInspector<>(this.grafo);
        Set<Album> connectedComponents = inspector.connectedSetOf(a);
        for(Album x : connectedComponents) {
        	durataTOT += x.getDurata();
        }
        return durataTOT/60000;
    }
	
	public Set<Album> ricercaSetMassimo(Album a1, double dTot) {
		
		if(a1.getDurata()>dTot) {
			return null;
		}
		//costruire insieme di parziale(che contenga già a1(era un vincolo della consegna)
		List<Album>parziale = new ArrayList<>();
		parziale.add(a1);
		List<Album>tutti = new ArrayList<>(this.getComponenteConnessa(a1));
		tutti.remove(a1);
		nAlbumMax = 1;
		this.migliore = new ArrayList<>(parziale);
		cerca(parziale, 1, dTot, a1.getDurata(), tutti);
		return new HashSet<>(migliore);
	}
	
	private Set<Album> getComponenteConnessa(Album a1) {
		ConnectivityInspector<Album, DefaultEdge> inspector = new ConnectivityInspector<>(this.grafo);
        Set<Album> connectedComponents = inspector.connectedSetOf(a1);
		return connectedComponents;
	}

	private void cerca(List<Album>parziale, int livello, double dTot, double durataParziale, List<Album>tutti) {
		//trovato soluz migliore
		if(parziale.size()>this.nAlbumMax) {
			this.nAlbumMax = parziale.size();
			this.migliore = new ArrayList<>(parziale);
		}
		for(Album nuovo: tutti) {
			if(!parziale.contains(nuovo) && (durataParziale+nuovo.getDurata()) <= dTot) {//qui posso cercare nuove soluzioni(se l'album non era gia inserito e la durata non supera il limite)
				parziale.add(nuovo);
				cerca(parziale, livello+1, durataParziale+nuovo.getDurata(), dTot, tutti);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}//per ottimizzare : 1- minimizzare numero di chiamate, 2- rendere le chiamate meno pesanti

	public Graph<Album, DefaultEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Album, DefaultEdge> grafo) {
		this.grafo = grafo;
	}

	public List<Album> getAllAlbum() {
		return allAlbum;
	}

	public void setAllAlbum(List<Album> allAlbum) {
		this.allAlbum = allAlbum;
	}
	
	
	
}
