package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class MainClass extends ApplicationAdapter {
	SpriteBatch batch;

	Texture play_btn;
	Texture pause_btn;
	Texture sun;

	//количество небесных тел
	private final int PLANET_COUNT = 3;
	Body[] planet = new Body[PLANET_COUNT];

	//переменые для расчета орбиты земли
	float earth_x;
	float earth_y;
	float earth_angle;
	double earth_r;

	//переменые для расчета орбиты луны
	float moon_x;
	float moon_y;
	float moon_angle;
	double moon_r;

	//переменые для расчета орбиты марса
	float mars_x;
	float mars_y;
	float mars_angle;
	double mars_r;

	//текущие координаты солнца
	float sun_x;
	float sun_y;

	//текущие координаты земли
	float earthX;
	float earthY;
	public float earth_getX() {
		return planet[0].position.x;
	}
	public float earth_getY() {
		return planet[0].position.y;
	}

	//текущие координаты луны
	float moonX;
	float moonY;
	public float moon_getX() {
		return planet[1].position.x;
	}
	public float moon_getY() {
		return planet[1].position.y;
	}

	//текущие координаты марса
	float marsX;
	float marsY;
	public float mars_getX() {
		return planet[2].position.x;
	}
	public float mars_getY() {
		return planet[2].position.y;
	}

	//логика кнопки плэй/пауза
	boolean play;
	boolean pause;

	//область нажатия кнопки плэй/пауза
	float button_max_x;
	float button_min_x;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//текстуры кнопки плэй/пауза
		pause_btn = new Texture("pause.png");
		play_btn = new Texture("play.png");

		//текстура солцна
		sun = new Texture("sun.png");

		//координаты солнца
		sun_x = Gdx.graphics.getWidth()/2 - 50;
		sun_y = Gdx.graphics.getHeight()/2 - 50;

		Body.setMyTexture(new Texture("earth.png"));
		//planet[1].setMoonTexture(new Texture("moon.png"));
		//planet[2].setMyTexture(new Texture("mars.png"));
		for (int i = 0; i < PLANET_COUNT; i++) {
			planet[i] = new Body(new Vector2(0, 0), new Vector2(0, 0));
		}

		play = true;
		pause = false;

	}

	@Override
	public void render () {
		update();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		//расчет области нажатия кнопки плэй/пауза
		button_max_x = Gdx.graphics.getWidth()/2 + pause_btn.getWidth()/2;
		button_min_x = Gdx.graphics.getWidth()/2 - pause_btn.getWidth()/2;

		//условия срабатывания нажатия кнопки плэй/пауза
		if(InputHandler.isClicked() && InputHandler.getMousePosition().x <= button_max_x && InputHandler.getMousePosition().x >= button_min_x && InputHandler.getMousePosition().y <= pause_btn.getHeight() && pause == false) {
			//play = false;
			pause = true;
		}
		if(InputHandler.isClicked() && InputHandler.getMousePosition().x <= button_max_x && InputHandler.getMousePosition().x >= button_min_x && InputHandler.getMousePosition().y <= play_btn.getHeight() && play == false && pause == true) {
			play = true;
			pause = false;
		}

		//условие старта и остановки расчета движения орбит
		if(play == true && pause == false) {

			//расчет орбиты земли
			earth_angle += 0.01;
			earth_r = 1.5;
			earth_x += earth_r * Math.cos(earth_angle);
			earth_y += earth_r * Math.sin(earth_angle);

			//расчет орбиты луны
			moon_angle += 0.03;
			moon_r = 1.5;
			moon_x += moon_r * Math.cos(moon_angle);
			moon_y += moon_r * Math.sin(moon_angle);

			//расчет орбиты марса
			mars_angle += 0.02;
			mars_r = 7;
			mars_x += mars_r * Math.cos(mars_angle);
			mars_y += mars_r * Math.sin(mars_angle);

		} else {
			pause();
		}

		//отрисовка солнца
		batch.draw(sun, sun_x, sun_y, 0, 0, 100, 100, 1.0f, 1.0f, 0, 0, 0, 512, 512, false, false);

		//отрисовка небесных тел
		for (int i = 0; i < PLANET_COUNT; i++) {
			planet[i].render(batch);
		}

		//получение текущих координат земли
		earthX = earth_getX();
		earthY = earth_getY();

		//получение текущих координат луны
		moonX = moon_getX();
		moonY = moon_getY();

		//получение текущих координат марса
		marsX = mars_getX();
		marsY = mars_getY();

		//начальные координаты и векторы движения по орбитам небесных тел
		planet[0] = new Body(new Vector2(500, 250), new Vector2(earth_x, earth_y));
		planet[1] = new Body(new Vector2(earthX - 0, earthY - 50), new Vector2(moon_x, moon_y));
		planet[2] = new Body(new Vector2(500, 50), new Vector2(mars_x, mars_y));

		//смена текстуры кнопки плэй/пауза
		if (pause == false) {
			batch.draw(pause_btn, Gdx.graphics.getWidth() / 2 - pause_btn.getWidth() / 2, 0);
		}
		if (play == false) {
			batch.draw(play_btn, Gdx.graphics.getWidth() / 2 - pause_btn.getWidth() / 2, 0);
		}

		batch.end();
	}

	public void update() {
		//обновление положения небесных тел
		for (int i = 0; i < PLANET_COUNT; i++) {
			planet[i].update();
		}
	}

	public void pause() {
		//запоминание координат положения небесных тел в момент паузы
		planet[0] = new Body(new Vector2(earthX, earthY), new Vector2(0, 0));
		planet[1] = new Body(new Vector2(moonX, moonY), new Vector2(0, 0));
		planet[2] = new Body(new Vector2(marsX, marsY), new Vector2(0, 0));

		play = false;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		pause_btn.dispose();
	}
}
