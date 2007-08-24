/**
 * @author wItspirit
 * 12-apr-2004
 * ParameterPanel.java
 */

package be.vanvlerken.bert.physics;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

/**
 * Provides a panel containing the GUI for manipulating the PhysicsParameters
 */
public class ParameterPanel extends JPanel implements ChangeListener
{
    private static final Logger logger = Logger.getLogger(ParameterPanel.class);

    private JPanel              runtimePanel;
    private JPanel              simRestartPanel;

    private JSpinner            spinnerMsBetweenIterations;
    private JSpinner            spinnerNrOfTimeSteps;
    private JSpinner            spinnerTimeStep;

    private JSpinner            spinnerGravityCte;
    private JCheckBox           checkboxApplyGravity;
    private JSpinner            spinnerNrOfElements;
    private JSpinner            spinnerMaxRadius;
    private JSpinner            spinnerMaxInitialSpeed;

    private JCheckBox checkboxTransparentBounds;

    public ParameterPanel()
    {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        initRuntimePanel();
        initSimRestartPanel();

        this.add(runtimePanel);
        this.add(simRestartPanel);
        this.add(Box.createVerticalGlue());
    }

    /**
     *  
     */
    private void initRuntimePanel()
    {
        runtimePanel = new JPanel();
        runtimePanel.setLayout(new BoxLayout(runtimePanel, BoxLayout.X_AXIS));
        runtimePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Runtime Changeable"));

        JPanel labelPanel = new JPanel(new GridLayout(6,1,5,5));        
        JPanel editorPanel = new JPanel(new GridLayout(6,1,5,5));

        runtimePanel.add(labelPanel);
        runtimePanel.add(editorPanel);

        addMsBetweenIterations(labelPanel, editorPanel);
        addNrOfTimeSteps(labelPanel, editorPanel);
        addTimeStep(labelPanel, editorPanel);
        addGravityCte(labelPanel, editorPanel);
        addApplyGravity(labelPanel, editorPanel);
        addTransparentBounds(labelPanel, editorPanel);

    }

    private void addApplyGravity(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelApplyGravity = new JLabel("Gravity");
        checkboxApplyGravity = new JCheckBox("apply");
        checkboxApplyGravity.setSelected(PhysicsParameters.applyGravity);
        checkboxApplyGravity.addChangeListener(this);

        labelPanel.add(labelApplyGravity);
        editorPanel.add(checkboxApplyGravity);
    }
    
    private void addTransparentBounds(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelTransparentBounds = new JLabel("Transparent Bounds");
        checkboxTransparentBounds = new JCheckBox("Make transparent");
        checkboxTransparentBounds.setSelected(PhysicsParameters.transparentBounds);
        checkboxTransparentBounds.addChangeListener(this);

        labelPanel.add(labelTransparentBounds);
        editorPanel.add(checkboxTransparentBounds);
    }

    private void addGravityCte(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelGravityCte = new JLabel("Gravity Cte");
        spinnerGravityCte = new JSpinner();
        spinnerGravityCte.setModel(new SpinnerNumberModel(
                PhysicsParameters.gravityCte, Double.MIN_VALUE,
                Double.MAX_VALUE, 0.01));
        spinnerGravityCte.addChangeListener(this);      
        spinnerGravityCte.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerGravityCte.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelGravityCte);
        editorPanel.add(spinnerGravityCte);
    }

    private void addTimeStep(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelTimeStep = new JLabel("Time Step");
        spinnerTimeStep = new JSpinner();
        spinnerTimeStep.setModel(new SpinnerNumberModel(
                PhysicsParameters.timeStep, 0, Double.MAX_VALUE, 0.0001));
        spinnerTimeStep.addChangeListener(this);
        spinnerTimeStep.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerTimeStep.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelTimeStep);
        editorPanel.add(spinnerTimeStep);
    }

    private void addNrOfTimeSteps(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelNrOfTimeSteps = new JLabel("# Time Steps");
        spinnerNrOfTimeSteps = new JSpinner();
        spinnerNrOfTimeSteps.setModel(new SpinnerNumberModel(
                PhysicsParameters.nrOfTimeSteps, 0, Integer.MAX_VALUE, 1));
        spinnerNrOfTimeSteps.addChangeListener(this);
        spinnerNrOfTimeSteps.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerNrOfTimeSteps.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelNrOfTimeSteps);
        editorPanel.add(spinnerNrOfTimeSteps);
    }

    private void addMsBetweenIterations(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelMsBetweenIterations = new JLabel("ms between interations");
        spinnerMsBetweenIterations = new JSpinner();
        spinnerMsBetweenIterations
                .setModel(new SpinnerNumberModel(
                        PhysicsParameters.msBetweenIterations, 0,
                        Integer.MAX_VALUE, 1));
        spinnerMsBetweenIterations.addChangeListener(this);
        spinnerMsBetweenIterations.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerMsBetweenIterations.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelMsBetweenIterations);
        editorPanel.add(spinnerMsBetweenIterations);
    }

    /**
     *  
     */
    private void initSimRestartPanel()
    {
        simRestartPanel = new JPanel();
        simRestartPanel.setLayout(new BoxLayout(simRestartPanel,
                BoxLayout.X_AXIS));
        simRestartPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Reset required"));

        JPanel labelPanel = new JPanel(new GridLayout(3,1,5,5));
        JPanel editorPanel = new JPanel(new GridLayout(3,1,5,5));

        simRestartPanel.add(labelPanel);
        simRestartPanel.add(editorPanel);

        addNrOfElements(labelPanel, editorPanel);
        addMaxRadius(labelPanel, editorPanel);
        addMaxInitialSpeed(labelPanel, editorPanel);

    }

    private void addNrOfElements(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelNrOfElements = new JLabel("# Elements");
        spinnerNrOfElements = new JSpinner();
        spinnerNrOfElements.setModel(new SpinnerNumberModel(
                PhysicsParameters.nrOfElements, 0, Integer.MAX_VALUE, 1));
        spinnerNrOfElements.addChangeListener(this);
        spinnerNrOfElements.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerNrOfElements.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelNrOfElements);
        editorPanel.add(spinnerNrOfElements);
    }

    private void addMaxRadius(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelMaxRadius = new JLabel("Max. radius");
        spinnerMaxRadius = new JSpinner();
        spinnerMaxRadius.setModel(new SpinnerNumberModel(
                PhysicsParameters.maxRadius, 0, Integer.MAX_VALUE, 1));
        spinnerMaxRadius.addChangeListener(this);
        spinnerMaxRadius.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerMaxRadius.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelMaxRadius);
        editorPanel.add(spinnerMaxRadius);
    }

    private void addMaxInitialSpeed(JPanel labelPanel, JPanel editorPanel)
    {
        JLabel labelMaxInitialSpeed = new JLabel("Max. initial speed");
        spinnerMaxInitialSpeed = new JSpinner();
        spinnerMaxInitialSpeed.setModel(new SpinnerNumberModel(
                PhysicsParameters.maxInitialSpeed, 0, Double.MAX_VALUE, 1));
        spinnerMaxInitialSpeed.addChangeListener(this);
        spinnerMaxInitialSpeed.setPreferredSize(PhysicsParameters.spinnerDim);
        spinnerMaxInitialSpeed.setMaximumSize(PhysicsParameters.spinnerDim);

        labelPanel.add(labelMaxInitialSpeed);
        editorPanel.add(spinnerMaxInitialSpeed);
    }

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource() == spinnerMsBetweenIterations)
        {
            PhysicsParameters.msBetweenIterations = ((SpinnerNumberModel) spinnerMsBetweenIterations
                    .getModel()).getNumber().intValue();
        }
        else if (e.getSource() == spinnerNrOfTimeSteps)
        {
            PhysicsParameters.nrOfTimeSteps = ((SpinnerNumberModel) spinnerNrOfTimeSteps
                    .getModel()).getNumber().intValue();
        }
        else if (e.getSource() == spinnerTimeStep)
        {
            PhysicsParameters.timeStep = ((SpinnerNumberModel) spinnerTimeStep
                    .getModel()).getNumber().doubleValue();
        }
        else if (e.getSource() == spinnerGravityCte)
        {
            PhysicsParameters.gravityCte = ((SpinnerNumberModel) spinnerGravityCte
                    .getModel()).getNumber().doubleValue();
        }
        else if (e.getSource() == checkboxApplyGravity)
        {
            PhysicsParameters.applyGravity = checkboxApplyGravity.isSelected();
        }
        else if (e.getSource() == checkboxTransparentBounds)
        {
            PhysicsParameters.transparentBounds = checkboxTransparentBounds.isSelected();
        }
        else if (e.getSource() == spinnerNrOfElements)
        {
            PhysicsParameters.nrOfElements = ((SpinnerNumberModel) spinnerNrOfElements
                    .getModel()).getNumber().intValue();
        }
        else if (e.getSource() == spinnerMaxRadius)
        {
            PhysicsParameters.maxRadius = ((SpinnerNumberModel) spinnerMaxRadius
                    .getModel()).getNumber().intValue();
        }
        else if (e.getSource() == spinnerMaxInitialSpeed)
        {
            PhysicsParameters.maxInitialSpeed = ((SpinnerNumberModel) spinnerMaxInitialSpeed
                    .getModel()).getNumber().doubleValue();
        }

    }
}