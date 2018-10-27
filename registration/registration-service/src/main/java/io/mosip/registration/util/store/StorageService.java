package io.mosip.registration.util.store;

import static io.mosip.kernel.core.util.DateUtils.formatDate;
import static io.mosip.registration.constants.LoggerConstants.LOG_PKT_STORAGE;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_ID;
import static io.mosip.registration.constants.RegistrationConstants.APPLICATION_NAME;
import static io.mosip.registration.constants.RegistrationConstants.ZIP_FILE_EXTENSION;
import static io.mosip.registration.constants.RegistrationExceptions.REG_IO_EXCEPTION;
import static java.io.File.separator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import io.mosip.kernel.core.spi.logger.MosipLogger;
import io.mosip.kernel.core.util.FileUtils;
import io.mosip.kernel.core.util.exception.MosipIOException;
import io.mosip.kernel.logger.logback.appender.MosipRollingFileAppender;
import io.mosip.kernel.logger.logback.factory.MosipLogfactory;
import io.mosip.registration.constants.RegistrationConstants;
import io.mosip.registration.exception.RegBaseCheckedException;
import io.mosip.registration.exception.RegBaseUncheckedException;

/**
 * Class to Store the Packets in local disk
 * 
 * @author Balaji Sridharan
 * @since 1.0.0
 *
 */
@Service
public class StorageService {

	private MosipLogger logger;

	@Autowired
	private Environment environment;@Autowired
	private void initializeLogger(MosipRollingFileAppender mosipRollingFileAppender) {
		logger = MosipLogfactory.getMosipDefaultRollingFileLogger(mosipRollingFileAppender, this.getClass());
	}
	/**
	 * Writes the encrypted packet to the local storage
	 * 
	 * @param registrationId
	 *            the id of the Registration
	 * @param packet
	 *            the encrypted packet data to be stored in local storage
	 * @param ackReceipt
	 *            the registration acknowledgement receipt to be stored in local
	 *            storage
	 * @return returns the file path where the files had been stored
	 * @throws RegBaseCheckedException
	 */
	public String storeToDisk(String registrationId, byte[] packet, byte[] ackReceipt) throws RegBaseCheckedException {
		try {
			// Generate the file path for storing the Encrypted Packet and Acknowledgement
			// Receipt
			String filePath = environment.getProperty(RegistrationConstants.PACKET_STORE_LOCATION) + separator
					+ formatDate(new Date(), environment.getProperty(RegistrationConstants.PACKET_STORE_DATE_FORMAT))
							.concat(separator).concat(registrationId);
			// Storing the Encrypted Registration Packet as zip
			FileUtils.copyToFile(new ByteArrayInputStream(packet), new File(filePath.concat(ZIP_FILE_EXTENSION)));
			logger.debug(LOG_PKT_STORAGE, APPLICATION_NAME, APPLICATION_ID,
					"Encrypted packet saved");
			// Storing the Registration Acknowledge Receipt Image
			FileUtils.copyToFile(new ByteArrayInputStream(ackReceipt),
					new File(filePath.concat("_Ack.").concat(RegistrationConstants.IMAGE_FORMAT)));
			logger.debug(LOG_PKT_STORAGE, APPLICATION_NAME, APPLICATION_ID,
					"Registration's Acknowledgement Receipt saved");
			return filePath;
		} catch (MosipIOException ioException) {
			throw new RegBaseCheckedException(REG_IO_EXCEPTION.getErrorCode(), REG_IO_EXCEPTION.getErrorMessage());
		} catch (RuntimeException runtimeException) {
			throw new RegBaseUncheckedException(RegistrationConstants.ENCRYPTED_PACKET_STORAGE,
					runtimeException.toString());
		}
	}
}
