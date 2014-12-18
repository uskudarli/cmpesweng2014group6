package Dutluk;

public class Theme {
	private int ThemeId;
	private String Name;
	public Theme(int id, String name)
	{
		this.ThemeId = id;
		this.Name = name;
	}
	public int getThemeId() {
		return ThemeId;
	}
	public void setThemeId(int themeId) {
		ThemeId = themeId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
}
