package de.ngloader.multiinventory.storage;

import net.imprex.storage.api.Storage;

public class LocaleStorage extends Storage<LocaleStorage> {

	@Override
	protected void connect() {
	}

	@Override
	protected void disconnect() {
	}

	@Override
	public boolean isOpen() {
		return true;
	}
}