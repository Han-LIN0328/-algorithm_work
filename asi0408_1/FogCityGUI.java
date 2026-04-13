import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// 1. 定義城市居民的結構
class Point {
    double x, y;
    String label;
    public Point(double x, double y, String label) {
        this.x = x; this.y = y; this.label = label;
    }
}

// 2. 定義鄰居，用來排序距離
class Neighbor implements Comparable<Neighbor> {
    double distance;
    Point point;
    public Neighbor(double distance, Point point) {
        this.distance = distance; this.point = point;
    }
    @Override
    public int compareTo(Neighbor other) {
        return Double.compare(this.distance, other.distance);
    }
}

// 記錄 KNN 決策結果
class DecisionResult {
    String finalLabel;
    List<Point> nearestPoints;
    double radius;
    public DecisionResult(String finalLabel, List<Point> nearestPoints, double radius) {
        this.finalLabel = finalLabel; this.nearestPoints = nearestPoints; this.radius = radius;
    }
}

public class FogCityGUI extends JFrame {

    private List<Point> city = new ArrayList<>();
    private Point player;
    
    // 當前狀態參數
    private int currentK = 1;
    private boolean isWeighted = false;
    private boolean showRadius = true;
    private DecisionResult currentResult;
    
    // 拖曳狀態控制
    private boolean isDragging = false;

    // UI 元件
    private JLabel mainDecisionLabel;
    private JLabel kValueLabel;
    private JLabel modeLabel;
    private JLabel statusLabel;
    private MapPanel mapPanel;

    public FogCityGUI() {
        initData();
        initUI();
        updateDecision();
    }

    // 初始化/重置城市資料
    private void initData() {
        city.clear();
        Random rand = new Random();
        
        generatePoints(20, 150, 250, 150, 300, "Exit A (Safe)", rand);
        generatePoints(20, 350, 450, 250, 320, "Exit B (Safe)", rand);
        generatePoints(25, 600, 750, 250, 450, "Exit C (Safe)", rand);
        generatePoints(35, 470, 560, 280, 380, "Danger (Wrong Way)", rand);

        // 調查員初始位置
        player = new Point(440, 330, "You");
    }

    private void generatePoints(int count, double xMin, double xMax, double yMin, double yMax, String label, Random rand) {
        for (int i = 0; i < count; i++) {
            double x = xMin + (xMax - xMin) * rand.nextDouble();
            double y = yMin + (yMax - yMin) * rand.nextDouble();
            city.add(new Point(x, y, label));
        }
    }

    // 介面設計
    private void initUI() {
        setTitle("霧城決策：KNN 生存模擬器 (互動版)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 244, 248));

        // --- 頂部狀態列 ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel titleLabel = new JLabel("霧城決策：KNN 生存模擬器");
        titleLabel.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 5));
        statsPanel.setOpaque(false);
        statsPanel.add(new JLabel("當前 K 值", SwingConstants.CENTER));
        statsPanel.add(new JLabel("權重模式", SwingConstants.CENTER));
        statsPanel.add(new JLabel("狀態", SwingConstants.CENTER));
        
        kValueLabel = new JLabel("1", SwingConstants.CENTER);
        modeLabel = new JLabel("標準多數決", SwingConstants.CENTER);
        statusLabel = new JLabel("計算中...", SwingConstants.CENTER);
        
        kValueLabel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        modeLabel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        statusLabel.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        
        statsPanel.add(kValueLabel);
        statsPanel.add(modeLabel);
        statsPanel.add(statusLabel);
        topPanel.add(statsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // --- 中間主視覺區 ---
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        
        mainDecisionLabel = new JLabel("決策：計算中...", SwingConstants.CENTER);
        mainDecisionLabel.setFont(new Font("微軟正黑體", Font.BOLD, 28));
        mainDecisionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        centerContainer.add(mainDecisionLabel, BorderLayout.NORTH);

        mapPanel = new MapPanel();
        mapPanel.setBackground(Color.WHITE);
        mapPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        
        // --- 加上滑鼠事件監聽器 ---
        setupMouseInteractions();

        JPanel mapWrapper = new JPanel(new BorderLayout());
        mapWrapper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        mapWrapper.setOpaque(false);
        mapWrapper.add(mapPanel, BorderLayout.CENTER);
        
        centerContainer.add(mapWrapper, BorderLayout.CENTER);
        add(centerContainer, BorderLayout.CENTER);

        // --- 底部控制列 ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setOpaque(false);
        
        JLabel comboLabel = new JLabel("情境選擇：");
        String[] options = { "第一夜: K=1 (標準)", "過渡: K=3 (標準)", "第二夜災難: K=5 (標準)", "生存策略: K=5 (距離加權)" };
        JComboBox<String> scenarioBox = new JComboBox<>(options);
        
        JCheckBox radiusToggle = new JCheckBox("顯示觀察半徑", true);
        radiusToggle.setOpaque(false);
        JButton resetBtn = new JButton("重置資料");
        
        scenarioBox.addActionListener(e -> {
            int index = scenarioBox.getSelectedIndex();
            if (index == 0) { currentK = 1; isWeighted = false; }
            else if (index == 1) { currentK = 3; isWeighted = false; }
            else if (index == 2) { currentK = 5; isWeighted = false; }
            else if (index == 3) { currentK = 5; isWeighted = true; }
            updateDecision();
        });

        radiusToggle.addActionListener(e -> {
            showRadius = radiusToggle.isSelected();
            mapPanel.repaint();
        });

        resetBtn.addActionListener(e -> {
            initData();
            updateDecision();
        });

        bottomPanel.add(comboLabel);
        bottomPanel.add(scenarioBox);
        bottomPanel.add(radiusToggle);
        bottomPanel.add(resetBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 設定滑鼠互動
    private void setupMouseInteractions() {
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 檢查是否點擊在調查員附近 (半徑 20 內視為點中)
                double dist = Math.hypot(e.getX() - player.x, e.getY() - player.y);
                if (dist <= 20) {
                    isDragging = true;
                } else {
                    // 如果點擊空白處，直接瞬間移動過去
                    player.x = e.getX();
                    player.y = e.getY();
                    updateDecision();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
            }
        });

        mapPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    // 限制拖曳範圍不要跑出畫布
                    int newX = Math.max(10, Math.min(e.getX(), mapPanel.getWidth() - 10));
                    int newY = Math.max(10, Math.min(e.getY(), mapPanel.getHeight() - 10));
                    
                    player.x = newX;
                    player.y = newY;
                    updateDecision(); // 邊拖邊即時更新
                }
            }
        });
    }

    private void updateDecision() {
        if (isWeighted) {
            currentResult = calculateWeightedKNN(currentK, player, city);
            modeLabel.setText("距離加權");
        } else {
            currentResult = calculateStandardKNN(currentK, player, city);
            modeLabel.setText("標準多數決");
        }

        kValueLabel.setText(String.valueOf(currentK));

        if (currentResult.finalLabel.contains("Safe")) {
            String safeText = "決策：安全";
            mainDecisionLabel.setText(safeText);
            mainDecisionLabel.setForeground(new Color(34, 139, 34));
            statusLabel.setText(safeText);
        } else {
            String dangerText = "決策：危險 (死胡同)";
            mainDecisionLabel.setText(dangerText);
            mainDecisionLabel.setForeground(new Color(200, 0, 0));
            statusLabel.setText(dangerText);
        }
        
        mapPanel.repaint();
    }

    // 演算法核心：標準 KNN
    private DecisionResult calculateStandardKNN(int k, Point player, List<Point> city) {
        List<Neighbor> neighbors = new ArrayList<>();
        for (Point p : city) neighbors.add(new Neighbor(Math.hypot(player.x - p.x, player.y - p.y), p));
        Collections.sort(neighbors);

        Map<String, Integer> voteCounts = new HashMap<>();
        List<Point> kNearest = new ArrayList<>();
        double radius = 0;

        for (int i = 0; i < k; i++) {
            Neighbor n = neighbors.get(i);
            kNearest.add(n.point);
            voteCounts.put(n.point.label, voteCounts.getOrDefault(n.point.label, 0) + 1);
            radius = n.distance;
        }

        String decision = "";
        int maxVotes = -1;
        for (Map.Entry<String, Integer> entry : voteCounts.entrySet()) {
            if (entry.getValue() > maxVotes) { maxVotes = entry.getValue(); decision = entry.getKey(); }
        }
        return new DecisionResult(decision, kNearest, radius);
    }

    // 演算法核心：距離加權 KNN
    private DecisionResult calculateWeightedKNN(int k, Point player, List<Point> city) {
        List<Neighbor> neighbors = new ArrayList<>();
        for (Point p : city) neighbors.add(new Neighbor(Math.hypot(player.x - p.x, player.y - p.y), p));
        Collections.sort(neighbors);

        Map<String, Double> scoreDict = new HashMap<>();
        List<Point> kNearest = new ArrayList<>();
        double radius = 0;

        for (int i = 0; i < k; i++) {
            Neighbor n = neighbors.get(i);
            kNearest.add(n.point);
            double weight = 1.0 / (n.distance + 1e-5);
            scoreDict.put(n.point.label, scoreDict.getOrDefault(n.point.label, 0.0) + weight);
            radius = n.distance;
        }

        String decision = "";
        double maxScore = -1.0;
        for (Map.Entry<String, Double> entry : scoreDict.entrySet()) {
            if (entry.getValue() > maxScore) { maxScore = entry.getValue(); decision = entry.getKey(); }
        }
        return new DecisionResult(decision, kNearest, radius);
    }

    // 自定義畫布
    class MapPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (Point p : city) {
                g2.setColor(getColorForLabel(p.label));
                g2.fillOval((int) p.x - 6, (int) p.y - 6, 12, 12);
            }

            g2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("北區安全出口", 150, 130);
            g2.drawString("中區安全出口", 350, 230);
            g2.drawString("南區安全出口", 600, 230);
            g2.setColor(new Color(200, 0, 0));
            g2.drawString("偏差危險區 (高密度)", 480, 420);

            if (currentResult != null && showRadius) {
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
                int r = (int) currentResult.radius;
                g2.drawOval((int) player.x - r, (int) player.y - r, r * 2, r * 2);

                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(Color.BLACK);
                for (Point p : currentResult.nearestPoints) {
                    g2.drawOval((int) p.x - 9, (int) p.y - 9, 18, 18);
                }
            }

            // 畫調查員
            g2.setColor(new Color(30, 144, 255));
            g2.fillOval((int) player.x - 8, (int) player.y - 8, 16, 16);
            
            g2.setColor(Color.WHITE);
            g2.fillRoundRect((int) player.x - 40, (int) player.y + 12, 80, 22, 10, 10);
            g2.setColor(new Color(30, 144, 255));
            g2.drawRoundRect((int) player.x - 40, (int) player.y + 12, 80, 22, 10, 10);
            
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString("(YOU)", (int) player.x - 34, (int) player.y + 27);
        }

        private Color getColorForLabel(String label) {
            if (label.contains("Exit A")) return new Color(30, 144, 255); 
            if (label.contains("Exit B")) return new Color(34, 139, 34);  
            if (label.contains("Exit C")) return new Color(218, 165, 32); 
            return new Color(178, 34, 34); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FogCityGUI().setVisible(true));
    }
}