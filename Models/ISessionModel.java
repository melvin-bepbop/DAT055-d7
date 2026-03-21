package Models;

import java.beans.PropertyChangeListener;
import java.util.LinkedList;

public interface ISessionModel {
    void addPropertyChangeListener(PropertyChangeListener pcl);
    void removePropertyChangeListener(PropertyChangeListener pcl);
    
    User getActiveUser();
    Channel getActiveChannel();
    AccesibleChannels getAccesibleChannels();
    LinkedList<MessagesInChannel> getMsgHistoryInChannels();
    LinkedList<Message> getHistoryForActiveChannel();
    
    void changeChannel(Channel newChannel);
    void addChannelHistory(MessagesInChannel msgInChnl);
    void appendMessagesToChannel(Channel channel, LinkedList<Message> msgs);
    void addAccessibleChannel(Channel channel);
}