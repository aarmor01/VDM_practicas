package com.example.app_android;

import com.example.engine_interfaces.IEngine;
import com.example.engine_interfaces.IRender;
import com.example.engine_interfaces.IScene;

public class Scene implements IScene {

    private int x;
    private int y;
    private int radius;
    private int speed;
    IEngine eng;

    @Override
    public void init(IEngine e) {
        this.x=100;
        this.y=0;
        this.radius = 100;
        this.speed = 150;
        this.eng = e;
    }

    @Override
    public void update(double deltaTime) {
        int maxX = eng.getRender().getWindowWidth()-this.radius;

        this.x += this.speed * deltaTime;
        this.y += 1;
        while(this.x < this.radius || this.x > maxX) {
            // Vamos a pintar fuera de la pantalla. Rectificamos.
            if (this.x < this.radius) {
                // Nos salimos por la izquierda. Rebotamos.
                this.x = this.radius;
                this.speed *= -1;
            } else if (this.x > maxX) {
                // Nos salimos por la derecha. Rebotamos
                this.x = 2 * maxX - this.x;
                this.speed *= -1;
            }
        }
    }

    @Override
    public void render(IRender renderMng) {
        renderMng.drawCircle(x, y, radius);
    }
}
