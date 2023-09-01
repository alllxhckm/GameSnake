import javax.swing.*;

public class Window extends JFrame
{
    public Window()
    {
        setTitle("Змейка");          // название окна
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);   //появление крестика для закрытия окна
        setSize(352, 377);     // размер окна
        setLocation(400, 400);   // положение окна на экране
        add(new GameField());      // подключение основного класса
        setVisible(true);    // появление окна
    }


    public static void main(String[] args)
    {
        Window w = new Window();
    }
}
