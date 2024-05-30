package registerable;

import java.rmi.Remote;
import java.rmi.RemoteException;

import participant.Participant;;

public interface Registerable extends Remote {
    void registerParticipant(Participant participant) throws RemoteException;
    String getParticipants() throws RemoteException;
}
