import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener
{
    private final int SIZE = 320;  //размер поля
    private final int DOT_SIZE = 16;  //размер одной клетки
    private final int ALL_DOTS = 400;   //всего клеток
    private Image dot;  //картинка змейки
    private Image apple;   // картинка фрукта
    private int appleX;  //координата Х фрукта
    private int appleY;    //координата Y фрукта
    private int[] x = new int[ALL_DOTS];  //положение змейки (Х)
    private int[] y = new int[ALL_DOTS];   //положение змейкки (Y)
    private int dots;  //количество ячеек змейки
    private Timer timer;   //скорость змейки
    private boolean left = false; //направление влево
    private boolean right = true; //направление вправо (изначально идёт вправо)
    private boolean up = false; //направление вверх
    private boolean down = false; //направление вниз
    private boolean inGame = true; //идёт игра или уже нет




    public GameField()    //вызов методов
    {
        setBackground(Color.black);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true); //соединение клавиш с игровым полем
    }


    public void initGame()   // инициализация начала игры
    {
        dots = 3;
        for (int i = 0; i < dots; i++)     //начальное положение змейки
        {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250,this);
        timer.start();
        createApple();        //вызов метода для создания фрукта в случайном месте
    }

    public void createApple()   //метод для создания фрукта в случайном месте
    {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
        appleInSnake();
    }

    public void appleInSnake()  //проверка чтобы фрукт не появлялся в теле змейки
    {
        boolean resX = false; boolean resY = false;
        for (int i = 0; i < x.length; i++)
        {
            if(x[i] == appleX)
            {
                resX = true;
            }
        }
        for (int i = 0; i < y.length; i++)
        {
            if(y[i] == appleY)
            {
                resY = true;
            }
        }
        if(resX && resY)
        {
            createApple();
        }
    }

public void loadImages()                //загружаем картинки в саму игру
    {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) //перерисовка
    {
        super.paintComponent(g);
        if(inGame)
        {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++)   //перерисовываем змейку на каждый ход
            {
                g.drawImage(dot, x[i], y[i], this);
            }
        }
        else
        {
            if(dots == 399) //если змейка занимает всю площадь - победа
            {
                String str = "You Win";
                g.setColor(Color.WHITE);
                g.drawString(str, 125, SIZE / 2);
            }
            else //конец игры
            {
                String str = "Game Over";
                g.setColor(Color.WHITE);
                g.drawString(str, 125, SIZE / 2);
            }
        }
    }

    public void move()   //движение змейки
    {
        for (int i = dots; i > 0; i--)  //точки следуют за головой
        {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if(left)     //движение головы
        {
            x[0] -= DOT_SIZE;
        }
        if(right)     //движение головы
        {
            x[0] += DOT_SIZE;
        }
        if(up)     //движение головы
        {
            y[0] -= DOT_SIZE;
        }
        if(down)     //движение головы
        {
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple()  //Съедение фрукта
    {
        if(x[0] == appleX && y[0] == appleY)
        {
            dots++;
            createApple();
        }
    }

    public void checkCollisions()  // проверка на столкновение
    {
        for (int i = dots; i > 0; i--)  //столкновение со своим телом
        {
            if(i > 4 && x[0] == x[i] && y[0] == y[i])
            {
                inGame = false;
            }
        }

        if(x[0] > SIZE)  //столкновение с границей
        {
            inGame = false;
        }
        if(x[0] < 0)  //столкновение с границей
        {
            inGame = false;
        }
        if(y[0] > SIZE)  //столкновение с границей
        {
            inGame = false;
        }
        if(y[0] < 0)  //столкновение с границей
        {
            inGame = false;
        }
        if(dots == 399)
        {
            inGame = false;
        }
    }



    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(inGame)
        {
            checkApple();  //проверка на сьедение фрукта
            checkCollisions();  //проверка на столкновения
            move();
        }
        repaint();
    }


    class FieldKeyListener extends KeyAdapter  //класс для обработки нажатия клавиш
    {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && !right)  //влево
            {
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && !left)  //вправо
            {
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && !down)  //вверх
            {
                left = false;
                up = true;
                right = false;
            }
            if(key == KeyEvent.VK_DOWN && !up)  //вниз
            {
                left = false;
                down = true;
                right = false;
            }
        }
    }
}
