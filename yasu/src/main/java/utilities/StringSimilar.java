package utilities;

public final class StringSimilar {
	public static float getDistance(String source, String target, int n) {
		int sl = source.length();
		int tl = target.length();
		if ((sl == 0) || (tl == 0)) {
			if (sl == tl) {
				return 1.0F;
			}
			return 0.0F;
		}
		int cost = 0;
		if ((sl < n) || (tl < n)) {
			int i = 0;
			for (int ni = Math.min(sl, tl); i < ni; i++) {
				if (source.charAt(i) == target.charAt(i)) {
					cost++;
				}
			}
			return cost / Math.max(sl, tl);
		}
		char[] sa = new char[sl + n - 1];
		for (int i = 0; i < sa.length; i++) {
			if (i < n - 1) {
				sa[i] = '\000';
			} else {
				sa[i] = source.charAt(i - n + 1);
			}
		}
		float[] p = new float[sl + 1];
		float[] d = new float[sl + 1];

		char[] t_j = new char[n];
		for (int i = 0; i <= sl; i++) {
			p[i] = i;
		}
		for (int j = 1; j <= tl; j++) {
			if (j < n) {
				for (int ti = 0; ti < n - j; ti++) {
					t_j[ti] = '\000';
				}
				for (int ti = n - j; ti < n; ti++) {
					t_j[ti] = target.charAt(ti - (n - j));
				}
			} else {
				t_j = target.substring(j - n, j).toCharArray();
			}
			d[0] = j;
			for (int i = 1; i <= sl; i++) {
				cost = 0;
				int tn = n;
				for (int ni = 0; ni < n; ni++) {
					if (sa[(i - 1 + ni)] != t_j[ni]) {
						cost++;
					} else if (sa[(i - 1 + ni)] == 0) {
						tn--;
					}
				}
				float ec = cost / tn;

				d[i] = Math.min(Math.min(d[(i - 1)] + 1.0F, p[i] + 1.0F), p[(i - 1)] + ec);
			}
			float[] _d = p;
			p = d;
			d = _d;
		}
		return 1.0F - p[sl] / Math.max(tl, sl);
	}
}
