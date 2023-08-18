package info.revington;

import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.JTextArea
import java.awt.Color

class LogWindow extends JFrame {

    private class StringOutputStream extends OutputStream {

        private StringBuilder builder = new StringBuilder();

        @Override
        void write(int b) throws IOException {
            builder.append((char)b);
        }

        public String read() {
            String content = builder.toString();

            builder = new StringBuilder();

            return content;
        }
    }

    private StringBuilder builder = new StringBuilder();

    private final StringOutputStream outputStream = new StringOutputStream();

    private final Process process;

    public LogWindow(Process process) {
        this.process = process;

        init()
    }

    public LogWindow(Process process, int x, int y) {
        this.process = process;
        this.setLocation(x, y)

        init()
    }

    private destroyProcess(ProcessHandle processHandle) {
        processHandle.children().forEach {
            destroyProcess(it)
        }

        outputStream.close()
        processHandle.destroyForcibly()
    }

    @Override
    void dispose() {
        destroyProcess(process.toHandle())
        outputStream.close()

        super.dispose()
    }

    private void init() {
        createWindow()

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE)
        this.setSize(600, 400)
        this.setVisible(true)

        process.waitForProcessOutput(outputStream, outputStream)

        this.dispose()
    }

    private void createWindow() {
        JTextArea area = new JTextArea()
        area.setSize(this.getSize())
        area.setLocation(0,0)
        area.setBackground(Color.BLACK)
        area.setForeground(Color.WHITE)
        area.setEditable(false)

        JScrollPane panel = new JScrollPane(area)

        new Thread(() -> {
            while (this.process.isAlive()) {
                synchronized (outputStream){
                    area.append(outputStream.read())
                }

                Thread.sleep(200)
            }
        }).start()

        this.setContentPane(panel)
    }
}