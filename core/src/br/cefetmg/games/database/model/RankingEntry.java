package br.cefetmg.games.database.model;

public class RankingEntry implements Comparable<RankingEntry> {

	private String name;
	private Integer points;

	public RankingEntry() {

	}

	public RankingEntry(String name, Integer points) {
		this.name = name;
		this.points = points;
	}

	public String getName() {
		return name;
	}

	public void setName(String user) {
		this.name = user;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	@Override
	public int compareTo(RankingEntry o) {
		return -this.getPoints().compareTo(o.getPoints());
	}
	
	@Override
	public String toString() {
		return "RankingEntry(name=\"" + this.getName() + "\", points=" + this.getPoints() + ")";
	}

}
