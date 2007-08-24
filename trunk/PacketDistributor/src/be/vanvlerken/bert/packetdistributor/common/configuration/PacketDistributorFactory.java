/**
 * @author wItspirit
 * 23-dec-2004
 * PacketDistributorFactory.java
 */

package be.vanvlerken.bert.packetdistributor.common.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import be.vanvlerken.bert.packetdistributor.common.IPacketDistributorAPI;
import be.vanvlerken.bert.packetdistributor.common.ITrafficDestination;
import be.vanvlerken.bert.packetdistributor.common.ITrafficEndpoint;
import be.vanvlerken.bert.packetdistributor.common.ITrafficRelay;
import be.vanvlerken.bert.packetdistributor.common.ITrafficSource;
import be.vanvlerken.bert.packetdistributor.common.MaximumExceededException;

/**
 * This factory will deliver instances of a PacketDistributorAPI, based on
 * configuration. It will also make sure that the PacketDistributorServer is
 * already bootstrapped in the sense that it will automatically instantiate all
 * preconfigured relays with their associated sources and destinations.
 */
public class PacketDistributorFactory
{
    private static final Log log = LogFactory.getLog(PacketDistributorFactory.class);
    private PDConfig         config;

    public PacketDistributorFactory()
    {
        this("packetdistributor.xml");
    }

    public PacketDistributorFactory(String configFile)
    {
        try
        {
            InputStream xmlStream = ConfigSupport.getResource(configFile);
            if (xmlStream == null)
            {
                log.warn(configFile + " could not be found. Reverting to dummy implementation");
                config = new PDConfig("be.vanvlerken.bert.packetdistributor.common.impl.dummy.PdDummyInterface");
            }
            else
            {
                config = PDConfig.getInstance(xmlStream);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public IPacketDistributorAPI getPacketDistributorAPI()
    {
        IPacketDistributorAPI api = getPacketDistributorImpl();
        if (api != null)
        {
            initializeApi(api);
        }
        return api;
    }

    /**
     * @param api
     * @throws MaximumExceededException
     */
    private void initializeApi(IPacketDistributorAPI api)
    {
        for (Relay relay : config.getRelays())
        {
            ITrafficRelay trafficRelay = api.createRelay(relay.getName());
            log.debug("Created Traffic Relay : id="+trafficRelay.getId()+" name="+trafficRelay.getName());
            for (Endpoint source : relay.getSources())
            {
                ITrafficSource trafficSource = api.createTrafficSource(source);
                log.debug("Created Traffic Source : IP="+trafficSource.getIpAddress().getHostAddress()+" Protocol="+trafficSource.getProtocol()+"("+(trafficSource.getProtocol() == ITrafficEndpoint.UDP ? "UDP" : "TCP")+") Port="+trafficSource.getPort());
                try
                {
                    trafficRelay.addTrafficSource(trafficSource);
                }
                catch (MaximumExceededException e)
                {
                    log.warn("Ignoring Traffic Source: " + e.getMessage());
                }
            }
            for (Endpoint destination : relay.getDestinations())
            {
                ITrafficDestination trafficDestination = api.createTrafficDestination(destination);
                log.debug("Created Traffic Destination : IP="+trafficDestination.getIpAddress().getHostAddress()+" Protocol="+trafficDestination.getProtocol()+"("+(trafficDestination.getProtocol() == ITrafficEndpoint.UDP ? "UDP" : "TCP")+") Port="+trafficDestination.getPort());
                try
                {
                    trafficRelay.addTrafficDestination(trafficDestination);
                }
                catch (MaximumExceededException e)
                {
                    log.warn("Ignoring Traffic Destination: " + e.getMessage());
                }
            }
        }
    }

    /**
     * @return
     */
    private IPacketDistributorAPI getPacketDistributorImpl()
    {
        IPacketDistributorAPI api = null;
        String apiName = config.getPacketDistributorImpl();        
        log.debug("PacketDistributorImpl provided by "+apiName);
        try
        {
            api = (IPacketDistributorAPI) Class.forName(apiName).newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return api;
    }
}
