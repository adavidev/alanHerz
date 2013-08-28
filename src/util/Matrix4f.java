/*
 *	 Herzog3D - 3D Real Time Strategy game.
 *   Copyright (C) 2005  Shannon Smith
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package util;

import java.nio.FloatBuffer;

public class Matrix4f {

	private static final int SIZE = 4;
	private final float[][] m = new float[SIZE][SIZE];

	public Matrix4f() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (i == j) {
					m[i][j] = 1;
				} else {
					m[i][j] = 0;
				}
			}
		}
	}

	public Matrix4f(float[][] m) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.m[i][j] = m[i][j];
			}
		}
	}

	public void setCol(int col, Vector4f v) {
		m[0][col] = v.x;
		m[1][col] = v.y;
		m[2][col] = v.z;
		m[3][col] = v.w;
	}

	public void setRow(int row, Vector4f v) {
		m[row][0] = v.x;
		m[row][1] = v.y;
		m[row][2] = v.z;
		m[row][3] = v.w;
	}

	public void transform(Vector4f v) {
		float x = v.x;
		float y = v.y;
		float z = v.z;
		float w = v.w;
		v.x = m[0][0] * x + m[0][1] * y + m[0][2] * z + m[0][3] * w;
		v.y = m[1][0] * x + m[1][1] * y + m[1][2] * z + m[1][3] * w;
		v.z = m[2][0] * x + m[2][1] * y + m[2][2] * z + m[2][3] * w;
		v.w = m[3][0] * x + m[3][1] * y + m[3][2] * z + m[3][3] * w;
	}

	public float determinant() {
		float f = m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
				+ m[0][1] * (m[1][2] * m[2][0] - m[1][0] * m[2][2])
				+ m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
		return f;
	}

	public void set(float[][] val) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				this.m[i][j] = val[i][j];
			}
		}
	}

	public float get(int row, int col){
		return m[row][col];
	}
	
	public boolean invert() {
		float determinant = determinant();
		if (determinant != 0) {
			/* do it the ordinary way
			 *
			 * inv(A) = 1/det(A) * adj(T), where adj(T) = transpose(Conjugate Matrix)
			 *
			 * m00 m01 m02
			 * m10 m11 m12
			 * m20 m21 m22
			 */
			float determinant_inv = 1f / determinant;
			// get the conjugate matrix
			float t00 = m[1][1] * m[2][2] - m[1][2] * m[2][1];
			float t01 = -m[1][0] * m[2][2] + m[1][2] * m[2][0];
			float t02 = m[1][0] * m[2][1] - m[1][1] * m[2][0];
			float t10 = -m[0][1] * m[2][2] + m[0][2] * m[2][1];
			float t11 = m[0][0] * m[2][2] - m[0][2] * m[2][0];
			float t12 = -m[0][0] * m[2][1] + m[0][1] * m[2][0];
			float t20 = m[0][1] * m[1][2] - m[0][2] * m[1][1];
			float t21 = -m[0][0] * m[1][2] + m[0][2] * m[1][0];
			float t22 = m[0][0] * m[1][1] - m[0][1] * m[1][0];

			m[0][0] = t00 * determinant_inv;
			m[1][1] = t11 * determinant_inv;
			m[2][2] = t22 * determinant_inv;
			m[0][1] = t10 * determinant_inv;
			m[1][0] = t01 * determinant_inv;
			m[2][0] = t02 * determinant_inv;
			m[0][2] = t20 * determinant_inv;
			m[1][2] = t21 * determinant_inv;
			m[2][1] = t12 * determinant_inv;

			return true;
		} else {
			return false;
		}
	}
	
	public void load(FloatBuffer buf){
		  m[0][0] = buf.get();
		  m[0][1] = buf.get();
		  m[0][2] = buf.get();
		  m[0][3] = buf.get();
		  m[1][0] = buf.get();
		  m[1][1] = buf.get();
		  m[1][2] = buf.get();
		  m[1][3] = buf.get();
		  m[2][0] = buf.get();
		  m[2][1] = buf.get();
		  m[2][2] = buf.get();
		  m[2][3] = buf.get();
		  m[3][0] = buf.get();
		  m[3][1] = buf.get();
		  m[3][2] = buf.get();
		  m[3][3] = buf.get();
	}
}
