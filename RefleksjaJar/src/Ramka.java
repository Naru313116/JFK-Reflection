import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Ramka extends JFrame {
    private int wys;
    private int szer;
    private int wysRamki;
    private int szerRamki;
    private static boolean plikZaladowany;

    private static DefaultListModel<Class<?>> zaladowaneKlasy;
    private static JList<Class<?>> listaKlas;
    private static Container kontener;

    private static JTextField opisMetod;
    private static JLabel tytul1;
    private static JLabel tytul2;
    private static JTextField arg1;
    private static JTextField arg2;
    private static JLabel tytulWynik;
    private static JTextField wynik;
    private static JButton run;

    Ramka() {
        szer = Toolkit.getDefaultToolkit().getScreenSize().width;
        wys = Toolkit.getDefaultToolkit().getScreenSize().height;
        setSize(szer / 2, wys / 2);
        szerRamki = getSize().width;
        wysRamki = getSize().height;

        setLocation((szer - szerRamki) / 2, (wys - wysRamki) / 2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        kontener = new Container();
        zaladowaneKlasy = new DefaultListModel<>();
        kontener.setPreferredSize(new Dimension(szerRamki, wysRamki));
        setContentPane(kontener);
        setResizable(false);
setMenu();
setClassesL();
initComponents();
setBackground(new Color(10,190,220));
pack();
    }


    protected void setMenu() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Załaduj plik");
        JMenuItem menuItem = new JMenuItem("typ .Jar");
        menuBar.add(menu);
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ladowanieKlas();
            }
        });
        //menuBar.setBounds(0,0,szerRamki,20);
        menuBar.setSize(szerRamki,20);
        menuBar.setLocation(0,0);
        kontener.add(menuBar);

    }

    protected void ladowanieKlas()
    {
        JFileChooser jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jFileChooser.setDialogTitle("Wybierz plik typu JAR");
        jFileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filtr = new FileNameExtensionFilter("Pliki typu JAR","jar");
        jFileChooser.addChoosableFileFilter(filtr);
        jFileChooser.showOpenDialog(this);

        String sciezka;
        JarFile plikJar = null;

        try{
            sciezka = jFileChooser.getSelectedFile().getAbsolutePath();
            plikJar = new JarFile(sciezka);
            Enumeration<JarEntry> entry = plikJar.entries();

            URL[] adresy = {new URL("jar:file:" + sciezka + "!/")};
            URLClassLoader loader = URLClassLoader.newInstance(adresy);

            zaladowaneKlasy.clear();

            while(entry.hasMoreElements())
            {
                JarEntry jarEntry = entry.nextElement();
                if(jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class"))
                {
                    continue;
                }

                String NazwaKlasy = jarEntry.getName().substring(0,jarEntry.getName().length()-6);
                NazwaKlasy = NazwaKlasy.replace('/',',');

                try{
                    Class<?> listaKlas = loader.loadClass(NazwaKlasy);

                    if(listaKlas.isAnnotationPresent(Description.class))
                    {
                        Description opis = (Description) listaKlas.getAnnotation(Description.class);

                        if(ICallable.class.isAssignableFrom(listaKlas))
                        {
                            ICallable callable = (ICallable) listaKlas.newInstance();

                            if(callable != null)
                            {
                               zaladowaneKlasy.addElement(listaKlas);
                            }
                        }

                    }
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            plikZaladowany= true;
            if(plikJar != null)
            {
                try
                {
                    plikJar.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

protected void setClassesL()
{
    listaKlas = new JList<>(zaladowaneKlasy);
    listaKlas.setBounds(0,45,200,wysRamki);
    listaKlas.setBackground(new Color(0,250,100));
listaKlas.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(listaKlas.getSelectedValue() != null)
        {
            String description = listaKlas.getSelectedValue().getAnnotation(Description.class).description();
            opisMetod.setText(description);
        }
    }
});
listaKlas.setFont(new Font("Arial", Font.PLAIN,15));
kontener.add(listaKlas);
}

protected void initComponents()
{
    opisMetod = new JTextField();
    opisMetod.setBounds(210,30,szerRamki-250,75);
    opisMetod.setEnabled(false);
    opisMetod.setBackground(Color.GRAY);
    opisMetod.setDisabledTextColor(Color.BLACK);
    opisMetod.setHorizontalAlignment(JTextField.CENTER);
    kontener.add(opisMetod);

    tytul1 = new JLabel("1 argument");
    tytul1.setBounds(270,150,80,25);
    tytul1.setForeground(Color.BLUE);
    tytul1.setFont(new Font("Arial", Font.BOLD, 15));
    kontener.add(tytul1);

    tytul2 = new JLabel("2 argument");
    tytul2.setBounds(600,150,80,25);
    tytul2.setForeground(Color.BLUE);
    tytul2.setFont(new Font("Arial", Font.BOLD, 15));
    kontener.add(tytul2);

    arg1 = new JTextField();
    arg1.setBounds(270,180,80,25);
    kontener.add(arg1);
    arg1.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if(!czyLiczba(e.getKeyChar())) e.consume();
        }
        private boolean czyLiczba(char zn) {
            if (zn >= '0' && zn <= '9') return true;
            return false;

        }
    });

    arg2 = new JTextField();
    arg2.setBounds(600,180,80,25);
    arg2.addKeyListener(new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if(!czyLiczba(e.getKeyChar())) e.consume();

        }
        private boolean czyLiczba(char znak)
        {
            if (znak >= '0' && znak <= '9') return true;
            return false;
        }
    });
    kontener.add(arg2);

    tytulWynik = new JLabel("Wynik:");
    tytulWynik.setForeground(Color.BLACK);
    tytulWynik.setFont(new Font("Arial", Font.PLAIN, 20));
    tytulWynik.setBounds(480,260,80,25);
    kontener.add(tytulWynik);

    wynik = new JTextField();
    wynik.setBounds(435,290,150,25);
    kontener.add(wynik);

    run = new JButton("RUN!");
    run.setBounds(szerRamki-140,wysRamki-80,100,25);
    run.addActionListener(e ->
    {
        if(plikZaladowany)
        {
            ICallable callable;
            String methodwynik;

            try
            {
                callable = (ICallable) listaKlas.getSelectedValue().newInstance();

                methodwynik = callable.call(arg1.getText(),arg2.getText());

                wynik.setText(methodwynik);
            } catch (InstantiationException | IllegalAccessException e1)
            {
                e1.printStackTrace();
            }
        }
        else
        {
            wynik.setText("Najpierw załaduj plik!");
        }
    });
    kontener.add(run);




}

}
